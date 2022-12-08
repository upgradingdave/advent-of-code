(ns day7
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(def fileName "files/day7_input.txt")

(defn parse-cmd [[path result] cmd]
  (cond (str/starts-with? cmd "$")
        (let [cmd (rest (str/split cmd #"\s"))]
          (cond (= (first cmd) "ls")
                [path result]

                (= (first cmd) "cd")
                (cond (= ".." (second cmd))
                      [(into [] (butlast path)) result]

                      :else
                      [(conj path (second cmd)) result]
                      )))

        ;; directory listing
        :else
        (let [listing (str/split cmd #"\s")]
          (cond (= (first listing) "dir")
                [path result]

                :else
                (let [size (Integer/parseInt (first listing))
                      [_ result] (reduce (fn [[npath result] n]
                                           (let [nnpath (str npath n)]
                                             [nnpath
                                              (update result
                                                      nnpath
                                                      (fn [x] (if x (+ x size) size)))]))
                                         ["" result]
                                         path)]
                  [path result])))))

(defn part1 []
  (let [lines (read-lines fileName)
        [_ dirs] (reduce parse-cmd [[] {}] lines)
        small-dirs (filter (fn [[k v]] (<= v 100000)) dirs)
        total (apply + (vals small-dirs))
        result total
        ]
    dirs))

(defn part2 []
  (let [lines (read-lines fileName)
        [_ dirs] (reduce parse-cmd [[] {}] lines)
        used (get dirs "/")
        unused (- 70000000 used)
        needed (- 30000000 unused)
        possible-dirs (filter (fn [[k v]] (>= v needed)) dirs)
        possible-sizes (sort (vals possible-dirs))
        ]
    (first possible-sizes)))