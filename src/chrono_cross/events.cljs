(ns chrono-cross.events
  (:require
   [re-frame.core :refer [reg-event-db]]
   [chrono-cross.model :as model]))

(reg-event-db
  :initialize
  (fn [db _]
    (merge db model/initial-state)))

(reg-event-db
 :next-state
 (fn
   [{:keys [snake board] :as db} _]
       (-> db
           (update-in [:snake] #(model/move-snake % board))
           (as-> after-move
               (model/process-move after-move)))))

(reg-event-db
 :change-direction
 (fn [db [_ new-direction]]
   (update-in db [:snake :direction]
              (partial model/change-snake-direction new-direction))))
