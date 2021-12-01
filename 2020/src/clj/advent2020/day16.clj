(ns advent2020.day16
  (:require [clojure.string :as str]))

(def sample1
  (str
   "class: 1-3 or 5-7\n"
   "row: 6-11 or 33-44\n"
   "seat: 13-40 or 45-50\n\n"
   "your ticket:\n"
   "7,1,14\n\n"
   "nearby tickets:\n"
   "7,3,47\n"
   "40,4,50\n"
   "55,2,20\n"
   "38,6,12\n"))

(def sample2
  (str
   "class: 0-1 or 4-19\n"
   "row: 0-5 or 8-19\n"
   "seat: 0-13 or 16-19\n\n"
   "your ticket:\n"
   "11,12,13\n\n"
   "nearby tickets:\n"
   "3,9,18\n"
   "15,1,5\n"
   "5,14,9\n"))

(def input-file-path "./resources/advent2020/day16.in")

(defn read-input [path-to-input-file]
  (slurp path-to-input-file))

(defn parse-field [line]
  (let [[_ n x y] (re-matches #"(.+):\s+(\d+-\d+)\s+or\s+(\d+-\d+)" line)]
    [n 
     (map #(let [[_ x1 x2] (re-matches #"(\d+)-(\d+)" %)]
             [(read-string x1) (read-string x2)]
             ;;(partial between? (read-string x1) (read-string x2))
             )
          [x y])]))

(defn parse-fields [fields-str]
  (let [lines (str/split fields-str #"\n")]
    (map parse-field lines)))

(defn parse-ticket-line [line]
  (let [numbers (str/split line #",")]
    (map read-string numbers)))

(defn parse-my-ticket [ticket-str]
  (parse-ticket-line (second (str/split ticket-str #"\n"))))

(defn parse-other-tickets [tickets-str]
  (let [lines (rest (str/split tickets-str #"\n"))]
    (map parse-ticket-line lines)))

(defn parse-input [input-str]
  (let [[fields-str ticket-str tickets-str]
        (str/split input-str #"\n\n")]
    {:fields (parse-fields fields-str)
     :ticket (parse-my-ticket ticket-str)
     :tickets (parse-other-tickets tickets-str)}))

(defn between? [min max value]
  (and (<= min value) (<= value max)))

(defn number-valid-for-field?
  "A field is name and a pair of ranges"
  [n field]
  (let [[field_name [[x1 x2] [y1 y2]]] field
        result (or (between? x1 x2 n)
                   (between? y1 y2 n))]
    result))

(defn completely-false?
  [n fields]
  (let [results (map #(vector (number-valid-for-field? n %) n) fields)
        total-false (count (filter (fn [[b _]] (false? b)) results))
        all-false? (= (count fields) total-false)]
    (if all-false? [true n] [false n])))

(defn day16a
  [input-str]
  (let [{:keys [fields ticket tickets] :as st} (parse-input input-str)]
    ;; look at each ticket and find tickets where one of the numbers
    ;; in the ticket is false for all the fields (`completly-false`
    ;; returns true)
    (let [results
          (reduce
           (fn [acc ticket]
             (let [results (map #(completely-false? % fields) ticket)]
               ;; only concerned with numbers that failed everything
               ;; where completely-false was true
               (concat acc (filter (fn [[b n]] (true? b)) results))))
           []
           tickets)]
      (apply + (map second results))
      )))


;; -------------
;; Part 2
(defn valid-tickets [tickets fields]
  (reduce
   (fn [acc ticket]
     (let [results2 (map #(completely-false? % fields) ticket)]
       ;; only concerned where completely-false is false
       (conj
        acc
        (map second
             (filter (fn [[b n]] (false? b)) results2)))))
   []
   tickets))

(defn valid-for-fields? [n fields]
  (into []
        (map (fn [field]
               (number-valid-for-field? n field))
             fields)))


;; combine two vectors like this:
;; [true true true] [true true true]
(defn combine2 [x y]
  (map (fn [[f s]] (and f s)) (partition 2 (interleave x y))))

;; combine two matrices like these two:
;; (combine [[false true true] [true true true] [true true true]] [[true true false] [true true true] [true true true]])
(defn combine [m1 m2]
  (map
   (fn [[f s]] (combine2 f s))
   (partition 2 (interleave m1 m2))))

(defn reduce-possibles [results]
  (reduce (fn [acc n] (combine acc n)) results))

(defn filter-true-cols [cols]
  (filter (fn [[_ col]] (true? col)) cols))

(defn update-known [[r cs] row col]
  (if (= r row)
    [r cs] ;; no change
    (let [new-cols (assoc cs col [col false])]
      [r new-cols])))

(defn update-knowns
  "set col to false for all rows besides `row`"
  [rows row col]
  (map (fn [[r cols]] (update-known [r cols] row col)) rows))

(defn find-solved-rows [knowns]
  (let [;; can we find any row that has 1 true column?
        possibles (filter
                   (fn [[idx cols]]
                     (= 1 (count (filter-true-cols cols)))) knowns)]
    ;; for each row with one true column, get the true column
    (map (fn [[row cols]] [row (filter-true-cols cols)])
         possibles)))

(defn solve-rows [knowns]
  (let [solved (find-solved-rows knowns)]
    (if (empty? solved)
      ;; if no solved-rows, then return knowns
      knowns

      ;; otherwise, update all the other columns
      ;; solved-rows looks like this:
      ;;([0 ([1 true])])
      ;; update all the other columns and try again
      (into []
            (reduce (fn [old-knowns next-answer]
                      (let [[r x] next-answer
                            [c _] (first x)]
                        (update-knowns old-knowns r c)))
                    knowns
                    solved))
      )))

(defn solve-column
  "is there only one true for this column across all rows?"
  [knowns col]
  (let [single-trues (filter (fn [[r [c b]]] (true? b))
                             (for [[row cols] knowns]
                               [row (get cols col)]))
        total (count single-trues)]
    (if (= 1 total)
      ;; for each row in single-trues, set all other columns in row to false
      ;; single trues looks like this
      ;;([2 [2 true]])
      (reduce
       (fn [new-knowns [r [c _]]]
         (assoc new-knowns
                r
                [r 
                 (into [] (map
                           (fn [[c1 b]] (if (= c1 c) [c1 true] [c1 false]))
                           (second (get new-knowns r))))]))
       knowns
       single-trues)

      ;; otherwise, just return knowns
      knowns
      )))

(defn solve-columns [knowns total-fields]
  (reduce
   (fn [old-knowns col] (solve-column old-knowns col))
   knowns
   (range total-fields)))

(defn solve [rows fields]
  (loop [knowns rows n 0]

    (let [solved (find-solved-rows knowns)]
      (cond

        (or (> n 100) (= (count solved) (count fields)))
        ;;knowns
        solved
        
        :else
        (let [
              ;; can we find case where col is true for only one row?
              knowns (solve-columns knowns (count fields))

              ;; can we find rows where only one col is true?
              knowns (solve-rows knowns)

              ]
          (recur knowns (inc n)))))))

(defn day16b
  [input-str]
  (let [{:keys [fields ticket tickets] :as st} (parse-input input-str)
        valids (valid-tickets tickets fields)
        ;; change values to booleans 
        results (reduce
                 (fn [acc valid]
                   (conj acc
                         (into [] (map #(valid-for-fields? % fields) valid))))
                 []
                 valids)
        possibles (reduce-possibles results)
        cols (map #(into [] (map-indexed vector %)) possibles)
        knowns (into [] (map-indexed vector cols))

        solution (solve knowns fields)

        results (reduce
                 (fn [acc [row [[col _]]]]
                   (conj acc [row (first (get (into [] fields) col))]))
                 []
                 solution)

        start-with-departure      
        (filter (fn [[_ n]] (re-matches #"departure.*" n)) results)
        ]

    ;; for some reason, `solve` doesn't solve the last field??
    ;; so departure-date is the 19th field
    [start-with-departure ticket]

))

(comment
  (day16a sample1)
  (day16a (read-input input-file-path))

  (day16b sample1)
  (day16b (read-input input-file-path))

      f1    f2    f3
  (n1(false true false)
   n2(true true false)
   n3(true true true)))
  



