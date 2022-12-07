(ns day4
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn split-lines [lines]
  (let [lines (map #(str/split % #",") lines)]
    lines))

(defn parse-section [section]
  (let [[_ min max] (re-matches #"(\d+)-(\d+)" section)]
    [(Integer/parseInt min) (Integer/parseInt max)]))

(defn parse-assignment [[one two]]
  [(parse-section one) (parse-section two)])

(defn sections-fully-overlap? [[[a1 a2] [b1 b2]]]
  (or (and (>= b1 a1) (<= b2 a2))
      (and (>= a1 b1) (<= a2 b2))))

(defn sections-overlap? [[[a1 a2] [b1 b2]]]
  (or (sections-fully-overlap? [[a1 a2] [b1 b2]])
      (and (>= a1 b1) (<= a1 b2))
      (and (>= a2 b1) (<= a2 b2))
      (and (>= b1 a1) (<= b1 a2))
      (and (>= b2 a1) (<= b2 a2))
      ))

(def fileName "files/day4_input1.txt")

(defn part1 []
  (let [lines (read-lines fileName)
        assignments (split-lines lines)
        parsed (map parse-assignment assignments)
        filtered (filter sections-fully-overlap? parsed)
        result (count filtered)
        ]
    result))

(defn part2 []
  (let [lines (read-lines fileName)
        assignments (split-lines lines)
        parsed (map parse-assignment assignments)
        filtered (filter sections-overlap? parsed)
        result (count filtered)
        ]
    result))

