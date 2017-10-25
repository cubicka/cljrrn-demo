(ns chrono-cross.model
  (:require [re-frame.core :refer [subscribe dispatch]]))

(def board [22 12])
(def snake {:direction [1 0]
            :body [[3 2] [2 2] [1 2] [0 2] [0 1] [0 0] [1 0]]})
(def possible-directions
  [[0 1] [1 0] [-1 0] [0 -1]])

(defn rand-free-position [snake [x y]]
  (let [snake-positions-set (into #{} (:body snake))
        board-positions (for [x-pos (range x)
                              y-pos (range y)]
                          [x-pos y-pos])]
    (when-let [free-positions (seq (remove snake-positions-set board-positions))]
      (rand-nth free-positions))))

(def initial-state {:board board
                    :snake snake
                    :food (rand-free-position snake board)
                    :points 0})

(defn process-move
  [{:keys [snake food board] :as db}]
  (if (= food (first (:body snake)))
    (-> db
        (update-in [:points] #(+ 13 %))
        (assoc :food (rand-free-position snake board)))
    db))

(defn change-snake-direction [[new-x new-y] [x y]]
  (if (or (= x new-x)
          (= y new-y))
    [x y]
    [new-x new-y]))

(defn abs [n] (max n (- n)))
(defn good-direction [head food prev-direction]
  (let [x-diff (- (first food) (first head))
        y-diff (- (second food) (second head))]
    (cond
      (and (>= (abs y-diff) (abs x-diff))
           (< 0 y-diff))
      [0 1]
      (and (>= (abs y-diff) (abs x-diff))
           (> 0 y-diff))
      [0 -1]
      (< 0 x-diff) [1 0]
      (> 0 x-diff) [-1 0]
      :else prev-direction)))

(defn move-randomly []
  (let [snake (subscribe [:snake])
        food (subscribe [:food])
        direction (subscribe [:direction])
        n (rand-int 50)]
    (when (< n 30)
      (dispatch [:change-direction
                 (rand-nth (concat possible-directions
                                   (repeat 7 (good-direction (first @snake) @food @direction))))]))))

(defn move-snake
  [{:keys [direction body] :as snake} boardDimension]
  (let [head-new-position (mapv #(+ %1 %2) direction (first body))
        [width height] boardDimension]
    (if (or (< (first head-new-position) 0)
            (<= width (first head-new-position))
            (< (second head-new-position) 0)
            (<= height (second head-new-position)))
      snake
    (update-in snake [:body] #(into [] (drop-last (cons head-new-position body)))))))
