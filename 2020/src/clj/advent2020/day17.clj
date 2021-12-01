(ns advent2020.day17
  (:require [clojure.string :as str]))

(def sample1
  [".#."
   "..#"
   "###"])

(def input-file-path "./resources/advent2020/day17.in")

(defn read-input [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n"))

(defn add-index [v]
  (map-indexed vector v))

(defn parse-lines [z lines]
  (vector
   (into
    []
    (map 
     (fn [[y line]]
       (into
        []
        (map (fn [[x c]] {:coord [x y z] :active? (= \# c)})
             (add-index line))))
     (add-index lines)))))

(defn get-cube [st [x y z]]
  (or
   (-> (get st z)
       (get y)
       (get x))
   {:coord [x y z] :active? false}))

(defn neighbor? [[x1 y1 z1] [x2 y2 z2]]
  (and (<= (Math/abs (- x2 x1)) 1)
       (<= (Math/abs (- y2 y1)) 1)
       (<= (Math/abs (- z2 z1)) 1)))

(defn neighbors [st [x y z]]
  (for [x1 (range (- x 1) (+ x 2))
        y1 (range (- y 1) (+ y 2))
        z1 (range (- z 1) (+ z 2))
        :when (not (and (= x x1) (= y y1) (= z z1)))]
    (get-cube st [x1 y1 z1])))

(defn active-ns [ns]
  (count (filter #(:active? %) ns)))

(defn update-cube
  "if cube active and 2 or 3 neighbors active, stay active, else inactive.
  if cube inactive and 3 neighbors are active, becomes active, else inactive"
  [st {:keys [active? coord] :as cube}]
  (let [ns (neighbors st coord)
        total (active-ns ns)]
    (if active?
      (assoc-in st (conj coord :active?) (or (= total 3) (= total 2)))
      (assoc-in st (conj coord :active?) (= total 3)))
    ))

;; when to stop??
(defn update-st [st]
  (loop [new-st st
         coord [0 0 0]
         remain #{}]
    ;; if this cube is inactive an all neighbors are also inactive,
    ;; then we're done, no need to recurse out another layer for this coord
    (let [ns (neighbors st coord)
          total (active-ns ns)]

      )))

(comment
  (def st (parse-lines 0 sample1))
  
  (day17a sample1)
  (day17a (read-input input-file-path))
)
  



