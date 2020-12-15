(ns advent2020.day15
  (:require [clojure.string :as str]))

(defn initialize [input]
  [(last input)
   (reduce
    (fn [acc [turn last-spoken]]
      (update acc last-spoken conj turn))
    {}
    (map (fn [[idx v]] [(inc idx) v]) (map-indexed vector input)))])

(defn take-turn [last-spoken st curr-turn]
  ;;(print last-spoken)
  (let [turns (get st last-spoken)]
    (if (or (nil? turns) (and (= (count turns) 1)
                              (= (first turns) (dec curr-turn))))
      ;; if not spoken before, then 0
      (let [next-spoken 0]
        ;;(println curr-turn ">>" last-spoken "first time spoken, so: 0")
        [next-spoken (update st next-spoken conj curr-turn)])
      ;; if spoken before, what's the age?
      (let [[t1 t2] turns
            age (- (or t1 0) (or t2 0))]
        ;;(println curr-turn ">>" last-spoken "already spoken" t1 "-" t2 "=" age)
        [age (update st age conj curr-turn)]))
    ))

(defn play [last-spoken st start-turn max-turn]
  (loop [last-spoken last-spoken st st turn start-turn]
    (if (> turn max-turn)
      last-spoken
      (let [[next-spoken next-st] (take-turn last-spoken st turn)]
        (recur next-spoken next-st (inc turn))))))

(defn day15 [turns max-turns]
  (let [[last-spoken st] (initialize turns)]
    (play last-spoken st (inc (count turns)) max-turns)))

(defn day15a []
  (day15 [0,3,1,6,7,5] 2020))

(defn day15b []
  (day15 [0,3,1,6,7,5] 30000000N))

(comment
  
  (= (day15 [0 3 6] 2020) 436)
  (= (day15 [1 3 2] 2020) 1)
  (= (day15 [2 1 3] 2020) 10)
  (= (day15 [1 2 3] 2020) 27)
  (= (day15 [2 3 1] 2020) 78)

  (day15a) ;; Part 1 success!

  (= 175594 (day15 [0 3 6] 30000000N))

  "Elapsed time: 35748.747546 msecs"
  (day15 [0,3,1,6,7,5] 20)

)


