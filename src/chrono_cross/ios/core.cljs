(ns chrono-cross.ios.core
  (:require [reagent.core :as r :refer [atom]]
            [re-frame.core :refer [dispatch dispatch-sync]]
            [chrono-cross.events]
            [chrono-cross.subs]
            [chrono-cross.model :as model]
            [chrono-cross.view :refer [game]]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))

(defn app-root []
  (fn []
    [game]))

(defn init []
      (dispatch-sync [:initialize])
      (.registerComponent app-registry "ChronoCross" #(r/reactify-component app-root)))

(defonce snake-moving
  (js/setInterval #(do
                     (model/move-randomly)
                     (dispatch [:next-state]))
                  150))
