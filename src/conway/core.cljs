(ns conway.core
  (:require [clojure.set :as set]))

(def glider
  #{[0 1]
    [1 0]
    [2 0]
    [2 1]
    [2 2]})

#_
(def gosper-glider-gun
  #{[1 4]
    [1 5]
    [2 4]
    [2 5]
    [11 3]
    [11 4]
    [11 5]
    [12 2]
    [12 6]
    [13 1]
    [13 7]
    [14 1]
    [14 7]
    [15 4]
    [16 2]
    [16 6]
    [17 3]
    [17 4]
    [17 5]
    [18 4]
    [21 5]
    [21 6]
    [21 7]
    [22 5]
    [22 6]
    [22 7]
    [23 4]
    [23 8]
    [25 3]
    [25 4]
    [25 9]
    [25 10]
    [34 6]
    [34 7]
    [35 6]
    [35 7]})

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

#_
(defn color [live-cells coord]
  (first (apply max-key val (frequencies (keep live-cells (neighbors coord))))))

(defn lives? [live-coords coord]
  (let [alive (contains? live-coords coord)]
    (if alive
      (some? (#{2 3} (count (alive-neighbors live-coords coord))))
      (= 3 (count (alive-neighbors live-coords coord))))))

(defn next-generation [live-coords]
  (let [coords-to-check (into live-coords (mapcat neighbors live-coords))]
    (into {} (filter #(lives? live-coords %) coords-to-check))))

(defn grid-size [live-coords]
  [[(apply min (map first (keys live-coords)))
    (apply min (map second (keys live-coords)))]
   [(apply max (map first (keys live-coords)))
    (apply max (map second (keys live-coords)))]])

#_
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

#_
(defn conway! [initial]
  (loop [state initial]
    (draw-grid state)
    (println "--------------")
    (Thread/sleep 10)
    (recur (next-generation state))))

#_
(comment

  (conway! gosper-glider-gun)

  )
