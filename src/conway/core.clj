(ns conway.core
  (:require [clojure.set :as set]))

(def initial-live-cells
  #{[0 0]
    [0 1]
    [0 -1]})

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

(defn alive-neighbors [live-cells coord]
  (set/intersection live-cells (neighbors coord)))

(defn lives? [live-cells coord]
  (let [alive (contains? live-cells coord)]
    (cond
      alive
      (some? (#{2 3} (count (alive-neighbors live-cells coord))))
      :else
      (= 3 (count (alive-neighbors live-cells coord))))))

(defn next-generation [live-cells]
  )
