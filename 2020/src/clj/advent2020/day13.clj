(ns advent2020.day13
  (:require [clojure.string :as str]))

(def sample1
  ["939"
   "7,13,x,x,59,x,31,19"])

(def sample2 [nil "17,x,13,19"])
(def sample3 [nil "67,7,59,61"])
(def sample4 [nil "67,x,7,59,61"])
(def sample5 [nil "67,7,x,59,61"])
(def sample6 [nil "1789,37,47,1889"])

(def input-file-path "./resources/advent2020/day13.in")

(defn read-input [path-to-input-file]
  (into []
        (str/split (slurp path-to-input-file) #"\n")))

(defn s->time [s]
  (if (= "x" s) nil (Integer/parseInt (str s))))

(defn process-input [lines]
  (let [estimate (Integer/parseInt (first lines))
        times (map s->time (str/split (second lines) #"\,"))]
    [estimate (filter identity times)]))

(defn next-time [estimate time]
  (+ (- (mod estimate time)) time estimate))

(defn day13a [input]
  (let [[estimate freqs] (process-input input)
        [best-freq best-time]
        (reduce (fn [[best-freq best-time] freq]
                  (let [time (next-time estimate freq)]
                    (if (or (nil? best-freq)
                            (< time best-time))
                      [freq time]
                      [best-freq best-time])))
                [nil nil]
                freqs
                )]
    (* best-freq (- best-time estimate))
    ))

;; ----------------------
;; Part 2
(defn valid? [guess [idx freq]]
  (= (mod (+ guess idx) freq) 0))

(defn all-valid? [guess freqs]
  (loop [f (first freqs) fs (rest freqs)]
    (cond

      (nil? f)
      true

      :else
      (when (valid? guess f)
        (recur (first fs) (rest fs))))))

(defn guess [freqs start step]
  (loop [g start]
    (cond
      (nil? g)
      :notfound

      (all-valid? g freqs)
      g

      :else
      (recur (+ g step))))
  )

(defn process-input2 [lines]
  (into []
        (let [times (map s->time (str/split (second lines) #"\,"))]
          (filter (fn [[_ f]] (not (nil? f))) (map-indexed vector times)))))

;; this works for all the sample inputs but was too slow for main input
;; (defn day13b [input]
;;   (let [freqs (process-input2 input)
;;         [bi bf] (reduce (fn [[i2 f2] [i f]] (if (> f f2) [i f] [i2 f2])) freqs)
;;         start (- (next-time above bf) bi)
;;         ]
;;     (guess freqs start start)
;;     ))

;; This was my lame attempt to derive the math formula to solve this
;; Correct answer 1068781

;; start at 7
;;(next-guess 7 (* 1 7) 13 1)
;; 14
;;(next-guess 14 (* 1 7 13) 59 4)
;; 5019
;;(next-guess 5019 (* 1 7 13 59) 31 6)
;; 96292
;; (next-guess 96292 (* 1 7 13 59 31) 19 8)
;; 1927121 correct answer, but not the min!!
;; (* 1 7 13 59 31 19) ;; 3162341

;; (defn next-guess [guess step curr offset]
;;   (loop [guess guess]
;;     (cond
;;       (= (mod guess curr) offset)
;;       guess

;;       :else
;;       (recur (+ guess step)))))

;; Chinese Remainder Theorem
;;https://www.geeksforgeeks.org/chinese-remainder-theorem-set-2-implementation/?ref=lbp
;; https://www.youtube.com/watch?v=ru7mWZJlRQg

(defn mod-mult-inv
  ;; Find the modular multiplicitave inverse of pp with respect to n
  [[_ pp] [_ f]]
  (loop [i 1]
    (if (= 1 (mod (* pp i) f)) i
        (recur (inc i)))))

;; still too slow!!
(defn chinese-remainder [input]
  (let [freqs (process-input2 input)
        prod (reduce (fn [acc [_ f]] (* acc f)) 1 freqs)
        pps (map (fn [[i f]] [i (/ prod f)]) freqs)
        invs (map (fn [[i pp]] (mod-mult-inv [i pp] (get freqs i))) pps)
        ]
    invs
))

(comment
  (day13a sample1)
  (day13a (read-input input-file-path))
)


