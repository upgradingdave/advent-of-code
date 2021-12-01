(ns advent2020.day1)

(def expenses
  [1721
   979
   366
   299
   675
   1456])

(def day1in "./resources/advent2020/day1.in")

(defn read-expenses [path-to-input-file]
  (map #(Integer/parseInt %)
       (clojure.string/split
        (slurp path-to-input-file) #"\n")))

(defn find2020 [expenses]
  (first
   (for [x (map-indexed vector expenses)
         y (map-indexed vector expenses)
         :when (and (not (= (first x) (first y)))
                    (= (+ (second x) (second y)) 2020))]
     (* (second x) (second y)))))

(defn find3 [expenses]
  (first
   (for [x (map-indexed vector expenses)
         y (map-indexed vector expenses)
         z (map-indexed vector expenses)
         :when (and (not (= (first x) (first y) (first z)))
                    (= (+ (second x) (second y) (second z)) 2020))]
     (* (second x) (second y) (second z)))))

(comment
  (find2020 (read-expenses day1in))
  (find3 (read-expenses day1in)))

