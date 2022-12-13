(ns day11
  (:require
    [clojure.string :as str]))

(defn read-lines [fileName]
  (with-open [rdr (clojure.java.io/reader fileName)]
    (doall (line-seq rdr))))

(defn toi [i]
  (Integer/parseInt (str i)))

(defn parse-line [[st curr] line]
  ;(println st curr line)
  (cond
    (str/starts-with? line "Monkey")
    (let [[_ idx] (re-matches #"Monkey\s+(\d+):" line)]
      [(assoc st (toi idx) {:inspects 0}) (toi idx)])

    (str/starts-with? line "Starting items")
    (let [[_ items] (re-matches #"Starting items:\s+(.+)" line)
          worries (mapv #(bigint (toi %)) (str/split items #"[, ]+"))]
      [(assoc-in st [curr :worries] worries) curr])

    (str/starts-with? line "Operation:")
    (let [[_ items] (re-matches #"Operation: new = (.+)" line)
          parts (str/split items #"\s+")
          operation (second parts)
          params [(first parts) (last parts)]]
      [(assoc-in st [curr :op] [operation params]) curr])

    (str/starts-with? line "Test:")
    (let [[_ n] (re-matches #"Test: divisible by (\d+)" line)
          dby (toi n)]
      [(assoc-in st [curr :test] dby) curr])

    (str/starts-with? line "If true:")
    (let [[_ n] (re-matches #"If true: throw to monkey (\d+)" line)
          monkey (toi n)]
      [(assoc-in st [curr :if-true] monkey) curr])

    (str/starts-with? line "If false:")
    (let [[_ n] (re-matches #"If false: throw to monkey (\d+)" line)
          monkey (toi n)]
      [(assoc-in st [curr :if-false] monkey) curr])

    :else
    [st nil]))

(defn run-op [worry [operation params]]
  (let [params (map #(if (= % "old") worry (toi %)) params)]
    (cond
      (= "+" operation)
      (apply + params)

      (= "*" operation)
      (apply * params)

      :else
      (throw (Exception. "Unsupported operation"))
      )))

(defn test-div-by [worry div]
  (= 0 (mod worry div)))

(defn throw-to [st worry to]
  (update-in st [to :worries] conj worry))

(defn inspect [st idx worry op div if-true if-false divs]
  (let [st1 (let [worry1 (run-op worry op)
                  worry2 (if divs
                           ;; part 2
                           (mod worry1 (apply * divs))
                           ;; part 1 div by 3
                           (quot worry1 3)
                           )]
              (if (test-div-by worry2 div)
                (throw-to st worry2 if-true)
                (throw-to st worry2 if-false)))]
    (update-in st1 [idx :inspects] inc)))

(defn turn [st idx {worries :worries op :op div :test if-true :if-true if-false :if-false} divs]
  (let [st1 (assoc-in st [idx :worries] [])]
    (reduce (fn [st worry] (inspect st idx worry op div if-true if-false divs)) st1 worries)))

(defn round [monkeys divs]
  (reduce
    (fn [st idx]
      (turn st idx (get st idx) divs))
    monkeys (keys monkeys)))

(defn rounds [monkeys rounds divs]
  (reduce (fn [st _] (round st divs)) monkeys rounds))

(def fileName "files/day11_input1.txt")

(defn part1 []
  (let [lines (read-lines fileName)
        cmds (map #(str/triml %) lines)
        [monkeys _] (reduce parse-line [{} nil] cmds)
        st (rounds monkeys (range 20) nil)
        inspects (map (fn [[k v]] (:inspects v)) st)
        most-active (take-last 2 (sort inspects))
        result (apply * most-active)
        ]
    result
    ))

(defn part2 []
  (let [lines (read-lines fileName)
        cmds (map #(str/triml %) lines)
        [monkeys _] (reduce parse-line [{} nil] cmds)
        divs (map (fn [[k v]] (:test v)) monkeys)
        st (rounds monkeys (range 10000) divs)
        inspects (sort (map (fn [[k v]] (:inspects v)) st))
        most-active (take-last 2 (sort inspects))
        result (apply * most-active)
        ]
    result
    )
  )

