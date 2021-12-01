(ns advent2020.day14
  (:require [clojure.string :as str]))

(def sample1
  ["mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"
   "mem[8] = 11"
   "mem[7] = 101"
   "mem[8] = 0"]
  )

(def input-file-path "./resources/advent2020/day14.in")

(defn read-input [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n"))

(defn parse-instruction [inst]
  (let [[_ loc d] (re-find #"mem\[(\d+)\] = (\d+)" inst)]
    [(read-string loc) (read-string d)]))

(defn parse-mask [m]
  (let [[_ result] (re-find #"mask = (.+)" m)]
    (into [] result)))

(defn dec->bin [d]
  (loop [d d b []]
    (let [n (quot d 2)]
      (if (<= n 0) (conj b (rem d 2))
          (recur n (conj b (rem d 2)))))))

(defn bin->dec [b]
  (let [pairs (partition 2 (interleave (reverse (range (count b))) b))]
    (apply + (map (fn [[p n]] (if (= 1 n) (Math/pow 2 p) 0)) pairs))))

(defn pad [b]
  (concat (for [_ (range (- 36 (count b)))] 0) (reverse b)))

(defn apply-mask [b mask]
  (reduce
   (fn [acc [b1 m1]]
     (conj acc
           (case m1
             \X b1
             \0 0
             \1 1)))
   []
   (partition 2 (interleave b mask))))

(defn update-mem [mem m [loc d]]
  (let [b (pad (dec->bin d))
        b1 (apply-mask b m)]
    (assoc mem loc (bin->dec b1))))

(defn run [lines]
  (loop [l (first lines)
         ls (rest lines)
         mem {}
         m nil]

    (cond
      (nil? l)
      mem

      (re-matches #"mem.+" l)
      (recur (first ls)
             (rest ls)
             (update-mem mem m (parse-instruction l))
             m)

      (re-matches #"mask.+" l)
      (recur (first ls) (rest ls) mem (parse-mask l))

      :else
      (throw (Exception. "Unknown command?"))
      )
    )
  )

(defn day14a [lines]
  (apply + (vals (run lines))))

(comment
  (day14a sample1)
  (day14a (read-input input-file-path))
)


