(ns day10
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn toi [i]
  (Integer/parseInt (str i)))

(def fileName "files/day10_input1.txt")

(defn run [cmds]
  (loop [sigs [] xs [] cycle 1 x 1 ctx nil cmds cmds]
    ;;(println cycle x ctx cmds)
    (if (empty? cmds)
      [sigs xs cycle x]
      ;; else
      (let [cmd (first cmds)]
        (cond
          (= (first cmd) "noop")
          (recur (conj sigs (* x cycle)) (conj xs x) (inc cycle) x nil (rest cmds))

          ;; addx
          :else
          (if (nil? ctx)
            (recur (conj sigs (* x cycle)) (conj xs x) (inc cycle) x 1 cmds)
            (let [x1 (+ x (toi (second cmd)))]
              (recur (conj sigs (* x cycle)) (conj xs x) (inc cycle) x1 nil (rest cmds)))))))))

(defn part1 []
  (let [lines (read-lines fileName)
        cmds (map #(str/split % #"\s") lines)
        [sigs _ _ _] (run cmds)
        getNth (partial nth sigs)
        result [(getNth 19) (getNth 59) (getNth 99) (getNth 139) (getNth 179) (getNth 219)]]
    (apply + result)
    ))

(defn draw-point [[i v]]
  (let [lit (and (>= v (- i 1)) (<= v (+ i 1)))
        c (if lit "#" ".")]
    ;;(println [i v lit])
    c))

(defn draw-line [line]
  (map draw line))

(defn part2 []
  (let [lines (read-lines fileName)
        cmds (map #(str/split % #"\s") lines)
        [_ xs _ _] (run cmds)
        rows (map #(map-indexed vector %) (partition 40 xs))
        lines (map draw-line rows)
        result (map #(apply str %) lines)
        ]
    result
    ))

