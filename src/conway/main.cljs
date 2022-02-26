(ns conway.main
  (:require
   [helix.core :refer [defnc $]]
   [helix.hooks :as hooks]
   [helix.dom :as dom]
   ["react-dom" :as ReactDOM]
   [conway.core :as conway]))

(def glider-initial-state
  {[0 1] "R"
   [1 0] "R"
   [2 0] "R"
   [2 1] "R"
   [2 2] "R"})

(defnc Root []
  (let [[state ->state] (hooks/use-state {})
        [[min-x min-y] [max-x max-y]] (conway/grid-size state)
        [playing ->playing] (hooks/use-state false)
        [interval ->interval] (hooks/use-state 500)]
    (hooks/use-effect
     [playing interval]
     (when playing
       (let [x (js/setInterval
                (fn []
                  (->state conway/next-generation))
                interval)]
         (fn []
           (js/clearInterval x)))))
    (dom/div
     (dom/button
      {:on-click
       (fn []
         (->playing not))}
      (if playing
        "Pause"
        "Play"))
     (dom/input
      {:value interval
       :on-change
       (fn [^js event]
         (->interval (.. event -target -value)))})
     (dom/table
      (dom/tbody
       (for [x (range -20 21)]
         (dom/tr
          {:key x}
          (for [y (range -20 21)]
            (let [coord [x y]]
              (dom/td
               {:key y
                :on-click
                (fn []
                  (if (state coord)
                    (->state dissoc coord)
                    (->state assoc coord "R")))
                :style
                {:width 10
                 :height 10
                 :background
                 (if (state coord)
                   "white"
                   "black")}}))))))))))

(defn ^:dev/after-load main []
  (ReactDOM/render ($ Root) (js/document.getElementById "root")))
