(ns advent2020.day18
  (:require [clojure.string :as str]))

(def sample1
  ["1 + 2 * 3 + 4 * 5 + 6"])

(def input-file-path "./resources/advent2020/day18.in")

(defn read-input [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n"))

(defn parse-parens [cs]
  (loop [c (first cs) cs (rest cs) total 1 acc []]
    (cond

      (= total 0)
      [(butlast acc) cs]

      (nil? c)
      [acc cs]
      
      (= \( c)
      (recur (first cs) (rest cs) (inc total) (conj acc c))

      (= \) c)
      (recur (first cs) (rest cs) (dec total) (conj acc c))

      :else
      (recur (first cs) (rest cs) total (conj acc c))))
  )

(defn parse-equation [cs]
  (loop [c (first cs) cs (rest cs) acc nil]
    (cond

      ;; if we're at the end or a end paren, then done!
      (nil? c)
      acc

      (= \( c)
      (let [[result cs] (parse-parens cs)
            total (parse-equation result)]
        (recur (first cs) (rest cs) (+ (or acc 0) total)))

      ;; do multiply
      (= \* c)
      (let [[_ n] cs]
        (if (= \( n)
          (let [[result cs] (parse-parens cs)
                total (parse-equation result)]
            (recur (first cs) (rest cs) (* (or acc 1) total)))
          (let [d2 (read-string (str n))]
            (recur (first (drop 2 cs)) (rest (drop 2 cs)) (* (or acc 1) d2)))))

      ;; do add
      (= \+ c)
      (let [[_ n] cs]
        (if (= \( n)
          (let [[result cs] (parse-parens cs)
                total (parse-equation result)]
            (recur (first cs) (rest cs) (+ (or acc 0) total)))
          (let [d2 (read-string (str n))]
            (recur (first (drop 2 cs)) (rest (drop 2 cs)) (+ (or acc 0) d2)))))

      ;; remember digit
      (re-matches #"\d" (str c))
      (recur (first cs) (rest cs) (read-string (str c)))
      
      ;; ignore spaces and everything else
      :else
      (recur (first cs) (rest cs) acc)

      )
    )
  )

(defn day18a [input]
  (apply + (map parse-equation input)))

(comment
  (day18a sample1)
  (day18a (read-input input-file-path))

  (= 71 (parse-equation "1 + 2 * 3 + 4 * 5 + 6"))
  (= 51 (parse-equation "1 + ( 2 * 3 ) + ( 4 * ( 5 + 6 ) )"))
  (= 51 (parse-equation "1 + (2 * 3) + (4 * (5 + 6))"))
  (= 437 (parse-equation "5 + (8 * 3 + 9 + 3 * 4 * 3)"))
  (= 252 (parse-equation "5 + 9 + 5 + 9 * 9"))
  (= 5670 (parse-equation "5 * (9 * 3 * (6 + 8 + 2 + 5) * 2)"))
    
  
)
  



