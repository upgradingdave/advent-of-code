(ns day2
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn win-lose-draw-score [[x y]]
  (cond (and (= x "A") (= y "Y")) 6
        (and (= x "B") (= y "Z")) 6
        (and (= x "C") (= y "X")) 6
        (and (= x "A") (= y "X")) 3
        (and (= x "B") (= y "Y")) 3
        (and (= x "C") (= y "Z")) 3
        :else 0))

(defn choice-score [[_ y]]
  (cond (= y "X") 1
        (= y "Y") 2
        (= y "Z") 3))

(defn round-score [r]
  (+ (win-lose-draw-score r) (choice-score r)))

(def fileName "files/day2_input1.txt")

(defn part1 []
  (let [lines (map #(str/split % #"\s+") (read-lines fileName))
        scores (map round-score lines)]
    (reduce + 0 scores)))

(defn convert [[x y]]
  (cond (= y "X")
        (cond (= x "A") [x "Z"]
              (= x "B") [x "X"]
              (= x "C") [x "Y"])

        (= y "Y")
        (cond (= x "A") [x "X"]
              (= x "B") [x "Y"]
              (= x "C") [x "Z"])

        (= y "Z")
        (cond (= x "A") [x "Y"]
              (= x "B") [x "Z"]
              (= x "C") [x "X"])))

(defn part2 []
  (let [lines (map #(str/split % #"\s+") (read-lines fileName))
        converted (map convert lines)
        scores (map round-score converted)]
    (reduce + 0 scores)))