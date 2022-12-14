(ns day13
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn toi [i]
  (Integer/parseInt (str i)))

(defn samesize [a b]
  (if (> (count a) (count b))
    [a (into [] (take (count a) (concat b (repeat nil))))]
    [(into [] (take (count b) (concat a (repeat nil)))) b]))

(defn correct? [l r]
  (cond
    (and (nil? l) (not (nil? r)))
    [:right-longer true]

    (and (not (nil? l)) (nil? r))
    [:left-longer false]

    (and (int? l) (int? r) (< l r))
    [:left-smaller true]

    (and (int? l) (int? r) (> l r))
    [:right-smaller false]

    (and (int? l) (int? r) (= l r))
    [:equal true]

    (and (int? l) (vector? r))
    (correct? [l] r)

    (and (vector? l) (int? r))
    (correct? l [r])

    (and (vector? l) (vector? r))
    (loop [c (apply mapv correct? (samesize l r))]
      (let [[reason result] (first c)
            next (rest c)]
        (cond
          (empty? next)
          [reason result]

          (= :left-smaller reason)
          [reason result]

          (= :equal reason)
          (recur next)

          :else
          [reason result]
          )))))

(def fileName "files/day13_input.txt")

(defn part1 []
  (let [lines (read-lines fileName)
        no-blanks (filter #(> (count %) 0) lines)
        forms (map read-string no-blanks)
        pairs (partition 2 forms)
        result (map #(apply correct? %) pairs)
        indexed (map-indexed vector result)
        trues (filter (fn [[_ [_ v]]] v) indexed)
        answer (map (fn [[k _]] (inc k)) trues)
        ]
    (apply + answer)))

(defn part2 [])

