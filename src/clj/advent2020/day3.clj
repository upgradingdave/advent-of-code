(ns advent2020.day3
  (:require [clojure.string :as str]))

(def squares
  ["..##......."
   "#...#...#.."
   ".#....#..#."
   "..#.#...#.#"
   ".#...##..#."
   "..#.##....."
   ".#.#.#....#"
   ".#........#"
   "#.##...#..."
   "#...##....#"
   ".#..#...#.#"]
  )

(def input-file-path "./resources/advent2020/day3.in")

(defn read-squares [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n"))

(defn tree? [squares x y]
  (let [w (count (first squares))
        h (count squares)
        i (mod x w)
        j (mod y h)]
    (if (>= y h)
      nil
      (= \# (get (get squares j) i)))))

(defn trees-in-slope [squares run rise]
  (count
   (filter true? 
           (for [y (range 0 (count squares)) :let [x (* (inc y) run)]]
             (tree? squares x (* (inc y) rise))))))

(defn day3a [squares]
  (trees-in-slope squares 3 1))

(defn day3b [squares]
  (apply *
         [(trees-in-slope squares 1 1)
          (trees-in-slope squares 3 1)
          (trees-in-slope squares 5 1)
          (trees-in-slope squares 7 1)
          (trees-in-slope squares 1 2)]))


(comment

  (= false (tree? squares 0 0))
  (= false (tree? squares 1 0))
  (= true  (tree? squares 2 0))
  (= nil  (tree? squares 2 11))

  (day3a squares)
  (day3a (read-squares input-file-path))

  (day3b squares)
  (day3b (read-squares input-file-path))
  
  )

