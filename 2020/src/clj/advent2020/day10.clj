(ns advent2020.day10
  (:require [clojure.string :as str]))

(def sample1
  [16 10 15 5 1 11 7 19 6 12 4])

(def sample2
  [28 33 18 42 31 14 46 20 48 47 24 23 49 45 19 38 39 11 1 32 25 35 8 17 7 9 4 2 34 10 3])

(def input-file-path "./resources/advent2020/day10.in")

(defn read-input [path-to-input-file]
  (into []
        (map #(read-string %) (str/split (slurp path-to-input-file) #"\n"))))

(defn process-input [nums]
  (into [] (map-indexed vector nums)))

(defn highest-joltage-rating [js]
  (+ 3 (apply max js)))

(defn possible-adapters [rating indexed-js]
  (when rating
    (filter #(and (>= (- (second %) rating) 1) (<= (- (second %) rating) 3))
            indexed-js)))

(defn remove-at-idx [v idx]
  (into []
        ;; this isn't good because it would need to be re-indexed every time
        ;;(concat (subvec v 0 idx) (subvec v (inc idx)))
        ;; try filter
        (filter (fn [[k v]] (not (= k idx))) v)))

(defn update-diffs [diffs difference]
  (update diffs difference #(if (nil? %) 1 (inc %))))

(defn search-for-possible [search indexed-js [results diffs]]
  (let [all-ps (possible-adapters search indexed-js)]

    (cond

      ;; if we made it thru the list of all jolts, we're done!
      (empty? indexed-js)
      [results diffs]

      ;; if there are no possibilities, but still some jolts, we failed
      (empty? all-ps)
      nil

      :else
      ;; pick the minimum possible and continue
      (let [p (reduce (fn [[k1 v1] [k2 v2]]
                        (if (< v1 v2) [k1 v1] [k2 v2])) all-ps)
            new-js (remove-at-idx indexed-js (first p))
            p-val (second p)
            result (search-for-possible
                    p-val
                    new-js
                    [(conj results p) (update-diffs diffs (- p-val search))]
                    )]
        result)
      

      )))

(defn day10a [jolts]
  (let [[results diffs] (search-for-possible 0 (process-input jolts) [[] {}])
        diffs (update-diffs diffs 3)
        ones (get diffs 1)
        threes (get diffs 3)]
    (* ones threes)
    ))

;; --------------------

(defn find-possibles [j js]
  (take-while #(<= % (+ j 3)) js))

(defn possibilities [j js]
  (cond

    ;; we've made it to the end of jolts, this is a possibility
    (empty? js)
    1

    ;; otherwise, let's see if we can find possible routes
    :else
    (let [possibles (find-possibles j js)]

      ;; if no possible routes, then this isn't a possibility
      (cond

        (empty? possibles)
        nil

        ;; otherwise, try all possible routes
        :else
        (apply + 
         (map
          (fn [[i2 v]]
            (possibilities v (drop (inc i2) js)))
          (map-indexed vector possibles)))
        
        )
      )
    )
  )

(defn day10b [jolts]
  (let [highest (highest-joltage-rating jolts)]
    (possibilities 0 (sort (conj jolts highest)))))

(comment
  (day10a sample1)
  (day10a (read-input input-file-path))

  (day10b sample1)p
  (day10b sample2)
  (day10b (read-input input-file-path))
  

  )


