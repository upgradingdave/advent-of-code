(ns day12
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn toi [i]
  (Integer/parseInt (str i)))

(defn neighbors [m {row :row col :col}]
  (let [orig [(get-in m [(dec row) col])
              (get-in m [(inc row) col])
              (get-in m [row (dec col)])
              (get-in m [row (inc col)])
              ]]
    (filter (comp not nil?) orig)))

(defn init [lines]
  (reduce (fn [result [row line]]
            (conj result
                  (mapv #(hash-map :row row :col (first %) :v (second %) :visited nil :dist nil)
                        (map-indexed vector line))))
          [] (map-indexed vector lines)))

(defn mark-start [{row :row col :col} m]
  (let [s {:v \a :dist 0 :visited nil :col col :row row :start true}]
    [s (assoc-in m [row col] s)]))

(defn mark-end [{row :row col :col} m]
  (let [e {:v \z :dist nil :visited nil :col col :row row :end true}]
    [e (assoc-in m [row col] e)]))

(defn update-dist [{a-dist :dist :as a} {b-dist :dist :as b}]
  (let [c-dist (inc a-dist)]
    (if b-dist
      (if (< c-dist b-dist)
        (assoc b :dist c-dist)
        b)
      ;; if b dist is nil
      (assoc b :dist c-dist))))

(defn remaining [m]
  (sort-by :dist (filter (comp not :visited) (filter :dist (flatten m)))))

(defn dijkstra [s m]
  (let [[s1 m1] (mark-start s m)]
    (loop [st m1 curr s1 cnt 0]
      (cond
        (:end curr)
        curr

        (nil? curr)
        curr

        :else
        (let [unvisited (filter (comp not :visited) (neighbors st curr))
              reachable (filter #(<= (- (int (:v %)) (int (:v curr))) 1) unvisited)
              updated (map #(update-dist curr %) reachable)
              st1 (reduce (fn [st {row :row col :col :as n}]
                            (assoc-in st [row col] n)) st updated)
              st2 (assoc-in st1 [(:row curr) (:col curr) :visited] true)
              remain (remaining st2)]
          (recur st2 (first remain) (inc cnt)))))))

(def fileName "files/day12_input.txt")

(defn part1 []
  (let [lines (read-lines fileName)
        m (init lines)
        s (first (filter #(= (:v %) \S) (flatten m)))
        [_ m1] (mark-end (first (filter #(= (:v %) \E) (flatten m))) m)
        result (dijkstra s m1)]
    result))

(defn find-starts [m]
  (filter #(= (:v %) \a) (flatten m)))

(defn dijkstras [starts m]
  (map (fn [s]
         (println "Attempting to run with start: " s)
         (dijkstra s m))
       starts))

(defn part2 []
  (let [lines (read-lines fileName)
        m (init lines)
        orig (first (filter #(= (:v %) \S) (flatten m)))
        m1 (assoc-in m [(:row orig) (:col orig)] {:v \a :dist nil :visited nil :col (:col orig) :row (:row orig)})
        [_ m2] (mark-end (first (filter #(= (:v %) \E) (flatten m1))) m1)
        starts (find-starts m2)
        result (dijkstras starts m2)
        answer (sort (map :dist result))
        ]
    answer))

