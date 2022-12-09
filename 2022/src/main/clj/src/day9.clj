(ns day9
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn toi [i]
  (Integer/parseInt (str i)))

(def fileName "files/day9_input.txt")

(defn step [[x y] d]
  (cond (= d "R")
        [(inc x) y]

        (= d "U")
        [x (inc y)]

        (= d "L")
        [(dec x) y]

        (= d "D")
        [x (dec y)]))

(defn touching? [[hx hy] [tx ty]]
  (and (<= (abs (- hx tx)) 1) (<= (abs (- hy ty)) 1)))

(defn step-tail [[hx hy] [tx ty]]
  (cond (touching? [hx hy] [tx ty])
        [tx ty]

        (and (= hx tx))
        (if (< hy ty)
          (step [tx ty] "D")
          (step [tx ty] "U"))

        (and (= hy ty))
        (if (< hx tx)
          (step [tx ty] "L")
          (step [tx ty] "R"))

        :else
        (if (< hx tx)
          (if (< hy ty)
            (step (step [tx ty] "L") "D")
            (step (step [tx ty] "L") "U"))
          (if (< hy ty)
            (step (step [tx ty] "R") "D")
            (step (step [tx ty] "R") "U")))))

(defn move [[hx hy] [tx ty] d]
  (let [[hx1 hy1] (step [hx hy] d)
        [tx1 ty1] (step-tail [hx1 hy1] [tx ty])]
    [[hx1 hy1] [tx1 ty1]]))

(defn run-n [[hx hy] [tx ty] visited dir n]
  (reduce (fn [[[hx hy] [tx ty] visited] dir]
            (let [[[hx1 hy1] [tx1 ty1]] (move [hx hy] [tx ty] dir)]
              [[hx1 hy1] [tx1 ty1] (conj visited [tx1 ty1])]))
          [[hx hy] [tx ty] visited] (repeat n dir)))

(defn run [moves]
  (reduce (fn [[[hx hy] [tx ty] visited] [dir dist]]
            (run-n [hx hy] [tx ty] visited dir dist))
          [[0 0] [0 0] #{}] moves))

(defn part1 []
  (let [lines (read-lines fileName)
        moves (map #(let [[d n] (str/split % #"\s")] [d (toi n)]) lines)
        [_ _ result] (run moves)]
    (count result)))

(defn step-long-rope [rope dir]
  (let [h (step (first rope) dir)]
    (reduce (fn [rope [tx ty]]
              (let [[hx hy] (last rope)
                    [tx1 ty1] (step-tail [hx hy] [tx ty])]
                (conj rope [tx1 ty1])
                ))
            [h] (rest rope))))

(defn move-long-rope-n [rope visited dir n]
  (reduce (fn [[rope visited] dir]
            (let [rope1 (step-long-rope rope dir)]
              [rope1 (conj visited (last rope1))]))
          [rope visited] (repeat n dir)))

(defn move-long-rope [moves]
  (reduce (fn [[rope visited] [dir n]]
            (move-long-rope-n rope visited dir n))
          [(repeat 10 [0 0]) #{}] moves))

(defn part2 []
  (let [lines (read-lines fileName)
        moves (map #(let [[d n] (str/split % #"\s")] [d (toi n)]) lines)
        [_ positions] (move-long-rope moves)]
    (count positions)))

