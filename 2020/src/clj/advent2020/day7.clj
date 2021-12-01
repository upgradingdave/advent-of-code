(ns advent2020.day7
  (:require [clojure.string :as str]))

(def rules
  ["light red bags contain 1 bright white bag, 2 muted yellow bags."
   "dark orange bags contain 3 bright white bags, 4 muted yellow bags."
   "bright white bags contain 1 shiny gold bag."
   "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags."
   "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags."
   "dark olive bags contain 3 faded blue bags, 4 dotted black bags."
   "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags."
   "faded blue bags contain no other bags."
   "dotted black bags contain no other bags."]
  )

(def input-file-path "./resources/advent2020/day7.in")

(defn read-rules [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n"))

(defn parse-bag-type [bag-type]
  (let [[n adj color _] (str/split (str/trim bag-type) #"\s")
        color-name (str adj " " color)
        color-key (keyword (str adj color))
        quantity (Integer/parseInt n)]
    [color-key {:color color-name
                :color-key color-key
                :quantity quantity}]))

(defn parse-contains [contains-value]
  (if (= contains-value " no other bags.")
    {}
    (let [bag-types (str/split contains-value #",")]
      (into {} (map parse-bag-type bag-types)))))

(defn parse-rule [rule]
  (let [[k v] (str/split rule #"contain")
        [adj color  _] (str/split k #"\s")
        color-name (str adj " " color)
        color-key (keyword (str adj color))]
    [color-key {:color-key color-key
                :color color-name
                :contains (parse-contains v)}]))

(defn parse-rules [rules]
  (into {}
        (reduce (fn [acc n] (conj acc (parse-rule n))) [] rules)))

(defn check-rule
  "Can bag-color-key eventually be inside this rule? This will return
  the color of the bag that eventually can contain search-key"
  [rules rule-key search-key]
  (let [{:keys [contains color color-key]} (get rules rule-key)]

    (cond
      ;; rule contains the bag color
      (get contains search-key)
      rule-key

      ;; rule contains no bags
      (empty? contains)
      false
      
      ;; rule doesn't contain the bag color, but can contain other bag types
      :else 
      (reduce (fn [acc n] (or acc n))
              false
              (map (fn [[k _]] (check-rule rules k search-key)) contains)))))

(defn day7a [rules]
  (count
   (filter identity
           (map (fn [[k v]] (check-rule rules k :shinygold)) rules))))

(defn count-bags
  [rules search-key]
  (let [{:keys [contains color color-key]} (get rules search-key)]
    
    (cond

      ;; rule contains no bags
      (empty? contains)
      0
      
      ;; rule contains more bags
      :else
      (apply + 
             (map (fn [[k v]]
                    (let [quantity (:quantity v)]
                      (+ quantity (* quantity (count-bags rules k)))))
                  contains))
      )))

(defn day7b [rules]
  (count-bags rules :shinygold))

(comment
  (day7a (parse-rules rules))
  (day7a (parse-rules (read-rules input-file-path)))

  (day7b (parse-rules rules))
  (day7b (parse-rules (read-rules input-file-path)))
  )
