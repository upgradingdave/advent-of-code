(ns day5
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn split-lines [lines]
  (let [lines (map #(str/split % #",") lines)]
    lines))

(defn addCrate [crates [idx crate]]
  (let [crateStack (get crates idx)]
    (if (= crate " ")
      crates
      (assoc crates idx (conj crateStack crate)))
    ))

(defn parseStartLines [lines]
  (let [digits (filter (partial re-matches #"\d+")
                      (str/split (first (filter #(str/starts-with? % " 1") lines)) #"\s"))
        total (Integer/parseInt (last digits))
        crates (into [] (repeat total []))
        start-lines (take-while (fn [n] (not (str/starts-with? n " 1"))) lines)
        rows (reduce (fn [result n]
                       (let [row (map second
                                      (re-seq #"\[([A-Z ])\]" (str/replace n #"    " " [ ]")))
                             indexed-row (map-indexed vector row)]
                         (conj result indexed-row))) [] start-lines)
        flat-rows (apply concat rows)
        result (reduce addCrate crates flat-rows)
        ]
    result))

(defn parseMoves [lines]
  (map (fn [line]
         (let [[_ x y z] (re-matches #"move (\d+) from (\d+) to (\d+)" line)]
           [(Integer/parseInt x) (Integer/parseInt y) (Integer/parseInt z)]))
       (filter (fn [n] (str/starts-with? n "move")) lines)))

(defn move [[_ from to] crates]
  (let [fromStack (get crates (dec from))
        toStack (get crates (dec to))
        crate (first fromStack)]
    (assoc
      (assoc crates (dec from) (into [] (rest fromStack)))
      (dec to) (into [] (concat [crate] toStack)))))

(defn move-n [crates [times from to]]
  (last (take (inc times) (iterate (partial move [times from to]) crates))))

(def fileName "files/day5_input1.txt")

(defn part1 []
  (let [lines (read-lines fileName)
        crates (parseStartLines lines)
        moves (parseMoves lines)
        result (reduce move-n crates moves)
        ]
    crates
    (apply str (map first result))
    ))

(defn move-bulk [crates [total from to]]
  (let [fromStack (get crates (dec from))
        toStack (get crates (dec to))
        cs (into [] (take total fromStack))]
    (assoc
      (assoc crates (dec from) (into [] (take-last (- (count fromStack) total) fromStack)))
      (dec to) (into [] (concat cs toStack)))))

(defn part2 []
  (let [lines (read-lines fileName)
        crates (parseStartLines lines)
        moves (parseMoves lines)
        result (reduce move-bulk crates moves)
        ]
    crates
    (apply str (map first result))
    ))
