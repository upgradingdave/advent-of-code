(ns day8
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn toi [i]
  (Integer/parseInt (str i)))

(def fileName "files/day8_input.txt")

(defn vis? [t x y [rows cols] w h]
  (cond
    ;; if tree is on edge, it's visible
    (or (<= x 0) (>= x (dec w)) (<= y 0) (>= y (dec h)))
    true

    :else
    (let [p (fn [n] (< n t))
          row (get rows y)
          col (get cols x)
          left (every? p (take x row))
          right (every? p (drop (inc x) row))
          up (every? p (take y col))
          down (every? p (drop (inc y) col))
          ]
      ;;[left right up down]
      (or left right up down)
      )))

(defn part1 []
  (let [lines (read-lines fileName)
        rows (reduce #(conj %1 (into [] (map toi (map conj %2)))) [] lines)
        cols (into [] (apply map vector rows))
        trees [rows cols]
        indexed (map-indexed vector (map #(map-indexed vector %) rows))
        result (map (fn [[y row]]
                      (map (fn [[x t]]
                             (vis? t x y trees (count row) (count rows))) row))
                    indexed)]
    (count (filter true? (flatten result)))))

(defn countTrees [t ts]
  (let [result (reduce (fn [r n] (if (< n t) (inc r) (reduced (inc r)))) 0 ts)]
    result))

(defn score [t x y [rows cols] w h]
  (cond
    ;; if tree is on edge, it's visible
    (or (<= x 0) (>= x (dec w)) (<= y 0) (>= y (dec h)))
    0

    :else
    (let [row (get rows y)
          col (get cols x)
          left (countTrees t (reverse (take x row)))
          right (countTrees t (drop (inc x) row))
          up (countTrees t (reverse (take y col)))
          down (countTrees t (drop (inc y) col))
          ]
      ;;[left right up down]
      (apply * [left right up down])
      )))

(defn part2 []
  (let [lines (read-lines fileName)
        rows (reduce #(conj %1 (into [] (map toi (map conj %2)))) [] lines)
        cols (into [] (apply map vector rows))
        trees [rows cols]
        indexed (map-indexed vector (map #(map-indexed vector %) rows))
        result (map (fn [[y row]]
                      (map (fn [[x t]]
                             (score t x y trees (count row) (count rows))) row))
                    indexed)]
    (apply max (flatten result))))