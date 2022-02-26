(ns conway.core
  (:require [clojure.set :as set]))

(def initial-state
  {[0 1] "R"
   [1 0] "R"
   [2 0] "R"
   [2 1] "R"
   [2 2] "R"})

(def gosper-glider-gun
  {[1 4] "R"
   [1 5] "R"
   [2 4] "R"
   [2 5] "R"
   [11 3] "R"
   [11 4] "R"
   [11 5] "R"
   [12 2] "R"
   [12 6] "R"
   [13 1] "R"
   [13 7] "R"
   [14 1] "R"
   [14 7] "R"
   [15 4] "R"
   [16 2] "R"
   [16 6] "R"
   [17 3] "R"
   [17 4] "R"
   [17 5] "R"
   [18 4] "R"
   [21 5] "R"
   [21 6] "R"
   [21 7] "R"
   [22 5] "R"
   [22 6] "R"
   [22 7] "R"
   [23 4] "R"
   [23 8] "R"
   [25 3] "R"
   [25 4] "R"
   [25 9] "R"
   [25 10] "R"
   [34 6] "R"
   [34 7] "R"
   [35 6] "R"
   [35 7] "R"})

(defn neighbors [coord]
  (let [[x y] coord]
    #{[(dec x) (inc y)]
      [x (inc y)]
      [(inc x) (inc y)]
      [(inc x) y]
      [(inc x) (dec y)]
      [x (dec y)]
      [(dec x) (dec y)]
      [(dec x) y]}))

(defn alive-neighbors [live-coords coord]
  (set/intersection live-coords (neighbors coord)))

(defn color [live-cells coord]
  (first (apply max-key val (frequencies (keep live-cells (neighbors coord))))))

(defn lives [live-cells coord]
  (let [live-coords (set (keys live-cells))
        alive (contains? live-coords coord)]
    (if alive
      (when (#{2 3} (count (alive-neighbors live-coords coord)))
        ;; Stays alive
        [coord (live-cells coord)])
      (when (= 3 (count (alive-neighbors live-coords coord)))
        ;; Resurrects
        [coord (color live-cells coord)]))))

(defn next-generation [live-cells]
  (let [live-coords (set (keys live-cells))
        coords-to-check (into live-coords (mapcat neighbors live-coords))]
    (into {} (keep #(lives live-cells %) coords-to-check))))

(defn grid-size [live-cells]
  [[(apply min (map first (keys live-cells)))
    (apply min (map second (keys live-cells)))]
   [(apply max (map first (keys live-cells)))
    (apply max (map second (keys live-cells)))]])

(defn draw-grid [live-cells]
  (let [live-coords (set (keys live-cells))
        [[min-x min-y] [max-x max-y]] (grid-size live-cells)]
    (doseq [y (range (- min-y 5) (+ max-y 5))]
      (doseq [x (range (- min-x 5) (+ max-x 5))]
        (let [coord [x y]]
          (if (contains? live-coords coord)
            (print (live-cells coord))
            (print "_"))))
      (println))))

(defn conway! [initial]
  (loop [state initial]
    (draw-grid state)
    (println "--------------")
    (Thread/sleep 1000)
    (recur (next-generation state))))

(comment

  (conway! initial-state)

  )
