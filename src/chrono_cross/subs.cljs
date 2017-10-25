(ns chrono-cross.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub :board
  (fn [db _] (:board db)))

(reg-sub :snake
  (fn [db _] (:body (:snake db))))

(reg-sub :food
  (fn [db _] (:food db)))

(reg-sub :score #(:points %))
(reg-sub :direction #(:direction (:snake %)))
