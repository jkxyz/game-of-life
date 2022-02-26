(ns conway.core
  (:require [clojure.set :as set]))

(def initial-live-cells
  #{[0 0]
    [0 1]
    [0 -1]})

(defn neighbors [coord]
  (let [[x y] coord]
    #{[(inc x) y]
      [(inc x) (dec y)]
      [x (dec y)]
      [(dec x) (dec y)]
      [(dec x) y]
      [(dec x) (inc y)]
      [x (inc y)]
      [(inc x) (inc y)]}))

(defn alive-neighbors [live-cells coord]
  (set/intersection live-cells (neighbors coord)))

(defn lives? [live-cells coord]
  (let [alive (contains? live-cells coord)]
    (cond
      alive
      (let [n (count (alive-neighbors live-cells coord))]
        (or (= 3 n) (= 2 n)))
      :else
      (= 3 (count (alive-neighbors live-cells coord))))))

(defn next-generation [live-cells]
  (set (filter #(lives? live-cells %) (into live-cells (mapcat neighbors live-cells)))))

(-> (next-generation initial-live-cells)
    (next-generation)
    (next-generation))
