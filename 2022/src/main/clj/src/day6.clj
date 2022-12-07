(ns day6
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(def fileName "files/day6_sample1.txt")

(defn solve [line len]
  (let [chunks (partition len 1 line)
        result (take-while (fn [n] (not (= (count (distinct n)) (count n)))) chunks)]
    (+ len (count result))
    ))

(defn part1 []
  (solve (first (read-lines fileName)) 4))

(defn part2 []
  (solve (first (read-lines fileName)) 14))


