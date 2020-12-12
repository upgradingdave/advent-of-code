(ns advent2020.day12
  (:require [clojure.string :as str]))

(def sample1
  ["F10"
   "N3"
   "F7"
   "R90"
   "F11"])

(def input-file-path "./resources/advent2020/day12.in")

(defn read-input [path-to-input-file]
  (into []
        (str/split (slurp path-to-input-file) #"\n")))

(defn parse-line [line]
  (let [[_ d v] (re-matches #"(\w)(\d+)" line)
        v (Integer/parseInt v)]
    (case d
      "N" [1 v]
      "E" [2 v]
      "S" [3 v]
      "W" [4 v]
      "F" [5 v]
      "L" [6 v]
      "R" [7 v]
      (throw (Exception. "invalid instruction"))
      )
    ))

(defn process-input [lines]
  (map parse-line lines))

(defn turn [deg d]
  (let [d' (+ d (/ deg 90))]
    (cond
      (> d' 4)
      (- d' 4)

      (< d' 1)
      (+ d' 4 )

      :else
      d')))

(defn move [v d x y]
  (cond

    ;; north
    (= d 1)
    [x (- y v)]

    ;; east
    (= d 2)
    [(+ x v) y]

    ;; south
    (= d 3)
    [x (+ y v)]

    ;; west
    (= d 4)
    [(- x v) y]

    :else
    (throw (Exception. "Invalid direction"))
    ))

(defn step [i v [d x y]]

  (cond

    ;; move North, South, East, or West
    (or (= i 1) (= i 2) (= i 3) (= i 4))
    (concat [d] (move v i x y))

    ;; forward
    (= i 5)
    (into [] (concat [d] (move v d x y)))

    ;; turn left
    (= i 6)
    [(turn (- 0 v) d) x y]

    ;; turn right
    (= i 7)
    [(turn v d) x y]

    ))

(defn day12a [dirs]
  (let [[d x y] (reduce
                 (fn [[d x y] [i v]] (step i v [d x y]))
                 [2 0 0]
                 (process-input dirs))]
    (+ x y)))

;; ---------- Part 2 -----------

(defn rotate
  "Rotate point p counter around origin o. negative deg is clockwise"
  [deg p o]
  (let [[px py] p
        [ox oy] o
        s  (Math/sin (Math/toRadians deg))
        c  (Math/cos (Math/toRadians deg))
        x1 (Math/round (+ ox (- (* c (- px ox)) (* s (- py oy)))))
        y1 (Math/round (+ oy (+ (* s (- px ox)) (* c (- py oy)))))
        ]
    [x1 y1]
    ))

(defn move-wp [v d x y]
  (cond

    ;; north
    (= d 1)
    [x (+ y v)]

    ;; east
    (= d 2)
    [(+ x v) y]

    ;; south
    (= d 3)
    [x (- y v)]

    ;; west
    (= d 4)
    [(- x v) y]

    :else
    (throw (Exception. "Invalid direction"))
    ))

(defn step-wp [i v [d x y x2 y2]]

  (cond

    ;; move the waypoint
    (or (= i 1) (= i 2) (= i 3) (= i 4))
    (into []
          (concat [d x y] (move-wp v i x2 y2)))

    ;; forward to the waypoint v times
    (= i 5)
    (let [xdist (* (- x2 x) v)
          ydist (* (- y2 y) v)]
      [d (+ x xdist) (+ y ydist) (+ x2 xdist) (+ y2 ydist)])

    ;; rotate waypoint left (counter clockwise) around x,y
    (= i 6)
    (into []
          (concat [d x y] (rotate v [x2 y2] [x y])))

    ;; rotate waypoint right (clockwise) around x,y
    (= i 7)
    (into []
          (concat [d x y] (rotate (- 0 v) [x2 y2] [x y])))

    ))

(defn day12b [dirs]
  (let [[d x y x2 y2]
        (reduce
         (fn [[d x y x2 y2] [i v]] (step-wp i v [d x y x2 y2]))
         [2 0 0 10 1]
         (process-input dirs))]
    (+ (Math/abs x) (Math/abs y))))

(comment
  (day12a sample1)
  (day12a (read-input input-file-path))

  (day12b sample1)
  (day12b (read-input input-file-path))
  )


