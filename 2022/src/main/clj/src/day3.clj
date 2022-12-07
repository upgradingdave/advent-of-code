(ns day3
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn split-lines [lines]
  (let [lines (map #(let [c (count %)
                          f (.substring % 0 (/ c 2))
                          s (.substring % (/ c 2))]
                      [f s]
                      )
                   (read-lines fileName))]
    lines))

(defn find-match [sack]
  (apply clojure.set/intersection (map set sack)))

(defn priority [c]
  (let [p (- (int c) 96)]
    (if (<= p 0)
      (+ (- (int c) 64) 26)
      p)))

(def fileName "files/day3_input1.txt")

(defn part1 []
  (let [lines (read-lines fileName)
        sacks (split-lines lines)
        matches (map find-match sacks)
        priorities (map priority (map first matches))
        result (apply + priorities)
        ]
    result))

(defn find-group-match [group]
  (->> group
       (map set)
       (apply clojure.set/intersection)))

(defn part2 []
  (let [lines (read-lines fileName)
        groups (partition 3 lines)
        matches (map find-group-match groups)
        priorities (map priority (map first matches))
        result (apply + priorities)]
    result))

