(ns advent2020.day5
  (:require [clojure.string :as str]))

(def passes
  ["BFFFBBFRRR"
   "FFFBBBFRRR"
   "BBFFBBFRLL"])

(def input-file-path "./resources/advent2020/day5.in")

(defn read-passes [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n"))

(defn find-mid [high low]
  (int (/ (inc (- high low)) 2)))

(defn parse-next [s low high lower upper]
  (loop [x (first s) xs (rest s) l low h high]
    (cond
      (= lower x)
      (recur (first xs) (rest xs) l (+ l (dec (find-mid h l))))
      
      (= upper x)
      (recur (first xs) (rest xs) (+ l (find-mid h l)) h)
      
      :else
      l)))

(defn parse-pass [pass]
  (let [row (parse-next (take 7 pass) 0 127 \F \B)
        col (parse-next (drop 7 pass) 0 7 \L \R)]
    [row col]))

(defn find-seat-id [row col]
  (+ (* row 8) col))

(defn day5a [passes]
  (let [seats  (map parse-pass passes)
        seat-ids (map #(apply find-seat-id %) seats)]
    (apply max seat-ids)))

;; I cheated on this one!
;; I should have done one extra step and filterd out any
;; seat-ids inisde `d` where a seat-id +1 or -1 exists
;; but instead, I just looked at the list ;-)
(defn day5b [passes]  
  (let [taken-seats (map parse-pass passes)
        taken-seat-ids (set (map #(apply find-seat-id %) taken-seats))

        possible-seats (for [r (range 0 128) c (range 0 8)] [r c])
        possible-seat-ids (set (map #(apply find-seat-id %) possible-seats))
        
        d (clojure.set/difference possible-seat-ids taken-seat-ids)]
    [(sort d) (count possible-seats) (count taken-seats)]))

(comment
  (parse-next "FBFBBFF" 0 127 \F \B)
  (parse-next "RLR" 0 7 \L \R)

  (parse-pass "FBFBBFFRLR")

  (day5a passes)
  (day5a (read-passes input-file-path))
  
  (day5b passes)
  (day5b (read-passes input-file-path))
  
  )

