(ns advent2020.day11
  (:require [clojure.string :as str]))

(def sample1
  ["L.LL.LL.LL"
   "LLLLLLL.LL"
   "L.L.L..L.."
   "LLLL.LL.LL"
   "L.LL.LL.LL"
   "L.LLLLL.LL"
   "..L.L....."
   "LLLLLLLLLL"
   "L.LLLLLL.L"
   "L.LLLLL.LL"])

(def sample2
  [".......#."
   "...#....."
   ".#......."
   "........."
   "..#L....#"
   "....#...."
   "........."
   "#........"
   "...#....."])

(def sample3
  [".##.##."
   "#.#.#.#"
   "##...##"
   "...L..."
   "##...##"
   "#.#.#.#"
   ".##.##."])

(def input-file-path "./resources/advent2020/day11.in")

(defn read-input [path-to-input-file]
  (into []
        (str/split (slurp path-to-input-file) #"\n")))

(defn idx->coords [w i]
  [(mod i w) (int (/ i w))])

(defn coords->idx [w [x y]]
  (+ (* y w) x))

(defn get-seat [seats [x y]]
  (get (get seats y) x))

(defn adjacent [[w h] [x y]]
  (let [left        (if (<  (dec x) 0) nil [(dec x) y])
        right       (if (>= (inc x) w) nil [(inc x) y])
        above       (if (<  (dec y) 0) nil [x (dec y)])
        below       (if (>= (inc y) h) nil [x (inc y)])
        above-left  (when (and above left)  [(dec x) (dec y)])
        above-right (when (and above right) [(inc x) (dec y)])
        below-left  (when (and below left)  [(dec x) (inc y)])
        below-right (when (and below right) [(inc x) (inc y)])]

    (filter identity
            [left right above below
             above-left above-right below-left below-right])))

(defn neighbors [seats [w h] [x y]]
  (map #(get-seat seats %) (adjacent [w h] [x y])))

(defn apply-rules [seats [w h] [x y]]
  (let [curr-seat (get-seat seats [x y])
        adjacents (neighbors seats [w h] [x y])]

    (cond

      ;; floor never changes
      (= \. curr-seat)
      [false curr-seat]

      ;; empty?
      (= \L curr-seat)
      (if (not (some #{\#} adjacents))
        [true \#]
        [false curr-seat])

      ;; occupied?
      (= \# curr-seat)
      (if (>= (count (filter #(= \# %) adjacents)) 4)
        [true \L]
        [false curr-seat])

      :else
      (throw (Exception. "Invalid value found in seat??")) 
      
      )))

(defn unzip [results]
  (reduce (fn [[bools vs] [b v]] [(or bools b) (conj vs v)])
          [false []]
          results))

(defn process-row [seats [w h] y]
  (let [results (for [x (range w)]
                  (apply-rules seats [w h] [x y]))
        [changed? vs] (unzip results)]
    [changed? (apply str vs)]))

(defn round [seats]
  (let [h (count seats)
        w (count (first seats))
        results (for [y (range h)] (process-row seats [w h] y))]
    (unzip results)))

(defn run-sim [seats]
  (loop [[changed? new-seats] (round seats)]
    (if changed?
      (recur (round new-seats))
      new-seats)))

(defn day11a [seats]
  (let [result (run-sim seats)]
    (apply + (for [r result] (count (filter #(= \# %) r))))))

;; -----------------

(defn next-visible [seats [w h] [x y] fnx fny]
  (loop [x (fnx x) y (fny y)]
    (cond
      ;; out of bounds
      (or (< x 0) (>= x w) (< y 0) (>= y h))
      nil

      :else
      (let [curr-seat (get-seat seats [x y])]
        (if (= curr-seat \.)
          ;; found a floor
          (recur (fnx x) (fny y))
          ;; found a seat
          curr-seat)))))

(defn visible-seats [seats [w h] [x y]]
  [
   ;; left
   (next-visible seats [w h] [x y] dec identity)

   ;; above left
   (next-visible seats [w h] [x y] dec dec)

   ;; above
   (next-visible seats [w h] [x y] identity dec)

   ;; above right
   (next-visible seats [w h] [x y] inc dec)

   ;; right
   (next-visible seats [w h] [x y] inc identity)

   ;; below right
   (next-visible seats [w h] [x y] inc inc)

   ;; below 
   (next-visible seats [w h] [x y] identity inc)

   ;; below left
   (next-visible seats [w h] [x y] dec inc)
   ]
  )

(defn apply-rules2 [seats [w h] [x y]]
  (let [curr-seat (get-seat seats [x y])
        visibles (visible-seats seats [w h] [x y])]

    (cond

      ;; floor never changes
      (= \. curr-seat)
      [false curr-seat]

      ;; empty?
      (= \L curr-seat)
      (if (not (some #{\#} visibles))
        [true \#]
        [false curr-seat])

      ;; occupied?
      (= \# curr-seat)
      (if (>= (count (filter #(= \# %) visibles)) 5)
        [true \L]
        [false curr-seat])

      :else
      (throw (Exception. "Invalid value found in seat??")) 
      
      )))

(defn process-row2 [seats [w h] y]
  (let [results (for [x (range w)]
                  (apply-rules2 seats [w h] [x y]))
        [changed? vs] (unzip results)]
    [changed? (apply str vs)]))

(defn round2 [seats]
  (let [h (count seats)
        w (count (first seats))
        results (for [y (range h)] (process-row2 seats [w h] y))]
    (unzip results)))

(defn run-sim2 [seats]
  (loop [[changed? new-seats] (round2 seats)]
    (if changed?
      (recur (round2 new-seats))
      new-seats)))

(defn day11b [seats]
  (let [result (run-sim2 seats)]
    (apply + (for [r result] (count (filter #(= \# %) r))))))

(comment
  (day11a sample1)
  (day11a (read-input input-file-path))

  (day11b sample1)
  (day11b (read-input input-file-path))

  )


