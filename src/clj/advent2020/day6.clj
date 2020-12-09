(ns advent2020.day6
  (:require [clojure.string :as str]))

(def answers "abc\n\na\nb\nc\n\nab\nac\n\na\na\na\na\n\nb")

(def input-file-path "./resources/advent2020/day6.in")

(defn read-answers [path-to-input-file]
  (slurp path-to-input-file))

(defn parse-answers [answers-str]
  (map #(str/split % #"\n")
       (str/split answers-str #"\n\n")))

(defn day6a [answers]
  (apply + 
         (map count
              (map #(into #{} %)
                   (map #(apply str %) answers)))))

(defn same-in-group [group-answers]
  (apply clojure.set/intersection (map set group-answers)))

(defn day6b [answers]
  (apply + 
         (map count
              (map same-in-group answers))))

(comment
  (day6a (parse-answers answers))
  (day6a (parse-answers (read-answers input-file-path)))

  (day6b (parse-answers answers))
  (day6b (parse-answers (read-answers input-file-path)))

  )

