(ns advent2020.day9
  (:require [clojure.string :as str]))

(def sample
  [35 20 15 25 47 40 62 55 65 95 102 117 150 182 127 219 299 277 309 576])

(def input-file-path "./resources/advent2020/day9.in")

(defn read-input [path-to-input-file]
  (into []
        (map #(bigint (read-string %))
             (str/split (slurp path-to-input-file) #"\n"))))


(defn check-number [preamble n]
  (let [possible (filter #(< % n) preamble)]
    ;;(println possible)
    (loop [x (first possible) xs (rest possible)]
      (if(nil? x)
        nil
        (let [results (for [y xs :when (= n (+ x y))] [x y])]
          ;;(println results)
          (if (empty? results)
            (recur (first xs) (rest xs))
            results))))))

(defn check [preamble-length numbers]
  (loop [i preamble-length]
    (let [preamble (take preamble-length (drop (- i preamble-length) numbers))
          n (get numbers i)]
      (if (nil? n)
        ;; can't find any weakness
        nil
        (let [result (check-number preamble n)]
          (if (nil? result)
            ;; found a weakness!
            n
            (recur (inc i))))))))

(defn day9a [preamble-length numbers]
  (check preamble-length numbers))

(defn find-contiguous [numbers test-number]
  (loop [i 1 x (first numbers) xs (rest numbers) acc 0]
    (if (nil? x)
      ;; couldn't find a valid answer?
      nil

      (let [total (+ x acc)]
        (cond

          ;; Found the continguous numbers!
          (= total test-number)
          (take i numbers)

          ;; if total is greater than n, then there's no valid answer
          (> total test-number)
          nil

          :else
          (recur (inc i) (first xs) (rest xs) total)
          )
        )
      )
    ))

(defn search-contiguous [numbers test-number]
  (loop [i 0]
    (if (>= i (count numbers))
      nil
      
      (let [result (find-contiguous (drop i numbers) test-number)]
        (if (nil? result)
          ;; try again
          (recur (inc i))
          ;; found solution!
          result
          )
        )
      )
    )
  )

(defn day9b [numbers test-number]
  (let [result (search-contiguous numbers test-number)
        sorted (sort result)]
    (+ (first sorted) (last sorted))))


(comment
  (day9a 5 sample)
  (day9a 25 (read-input input-file-path))

  (day9b sample 127)
  (day9b (read-input input-file-path) 1309761972N)
    
  )
