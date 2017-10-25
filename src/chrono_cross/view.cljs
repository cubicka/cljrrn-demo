(ns chrono-cross.view
    (:require 
        [reagent.ratom :refer [reaction]]
        [reagent.core :as r :refer [atom]]
        [re-frame.core :refer [subscribe dispatch]]
        [goog.events :as events]))

(def ReactNative (js/require "react-native"))

(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))

(def styles
    {:row {:flex 1 :flex-direction "row" :justify-content "space-between"}
     :cell-empty {:flex 1 :background-color "white" :border-color "#ccc" :border-width 1}
     :cell-snake {:background-color "skyblue"}
     :cell-food {:background-color "green"}
     :cell-head {:background-color "red"}
     
     :direction {:flex 0 :background-color "#eee" :align-items "center"}})

(defn styles-merge [& style-names]
    (->> style-names (map styles) (apply merge)))

(defn cell-empty []
    [view {:style (styles :cell-empty)}])

(defn cell-snake []
    [view {:style (styles-merge :cell-empty :cell-snake)}])

(defn cell-food []
    [view {:style (styles-merge :cell-empty :cell-food)}])

(defn cell-head []
    [view {:style (styles-merge :cell-empty :cell-head)}])

(defn row [& childrens]
    [view {:style (styles :row)}
        childrens])

(defn board [board-dimension snake food]
  (let [[width height] board-dimension
        snake-positions (into #{} snake)]
    [view {:style {:flex 1 :flex-direction "column"}}
        (for [y (range height)]
          ^{:key y}
           [row (for [x (range width)
                      :let [current-pos [x y]]]
                  (cond
                    (= current-pos (first snake)) ^{:key x} [cell-head]
                    (snake-positions current-pos) ^{:key x} [cell-snake]
                    (= current-pos food) ^{:key x} [cell-food]
                    :else ^{:key x} [cell-empty]))])]))

(defn render-board []
    (let [board-dimension (subscribe [:board])
          snake (subscribe [:snake])
          food (subscribe [:food])]
      (board @board-dimension @snake @food)))

(def style-for-direction-text
    {:flex 0 :height 40 :text-align "center" :line-height 40})

(defn game-board [score]
    [view {:style {:flex 1}}
        [view {:style {:flex 0 :height 20}}]
        [view {:style (styles :direction)}
            [touchable-highlight {:on-press #(dispatch [:change-direction [0 -1]])}
                [text {:style style-for-direction-text} "UP"]]]
        [view {:style {:flex 1 :flex-direction "row"}}
            [view {:style {:flex 0 :width 40 :justify-content "center" :background-color "#eee" :align-items "center"}}
                [touchable-highlight {:on-press #(dispatch [:change-direction [-1 0]])}
                    [text {:style style-for-direction-text} "<"]]]
            [render-board]
            [view 
                [touchable-highlight 
                    [text {:style style-for-direction-text} ">"]]]]
        [view {:style (styles :direction)}
            [touchable-highlight {:on-press #(dispatch [:change-direction [0 1]])}
                [text {:style style-for-direction-text} "DOWN"]]]
        [view
            [text {:style {:height 30 :line-height 30 :padding-right 10 :font-size 14 :text-align "right"}} score]]])

(defn game []
  (let [score (subscribe [:score])]
    [game-board @score]))
