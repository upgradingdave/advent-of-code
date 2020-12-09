(ns advent2020.day2
  (:require [clojure.string :as str]))

(def rules
  ["1-3 a: abcde"
   "1-3 b: cdefg"
   "2-9 c: ccccccccc"
   ])

(def day2in "./resources/advent2020/day2.in")

(defn read-rules [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n"))

(defn parse-rule [rule]
  (let [[part1 val] (str/split rule #":")
        [freq letter] (str/split part1 #"\s+")
        [_ x y] (re-find #"(\d+)-(\d+)" freq)
        x (Integer/parseInt x)
        y (Integer/parseInt y)
        val (str/trim val)]
    [x y letter val]))

(defn check-rule [rule]
  (let [[x y letter val] (parse-rule rule)
        filtered (filter #(= letter (str %)) val)
        total (count filtered)]
    (and (>= total x) (<= total y))))

(defn check-rule2 [rule]
  (let [[x y letter val] (parse-rule rule)]
    (if (= letter (str (nth val (dec x))))
      (not (= letter (str (nth val (dec y)))))
      (= letter (str (nth val (dec y)))))))

(defn day2a [rules]
  (count (filter true? (map check-rule rules))))

(defn day2b [rules]
  (count (filter true? (map check-rule2 rules))))

(comment
  (day2a rules)
  (day2a (read-rules day2in))

  (day2b rules)
  (day2b (read-rules day2in))
  )

