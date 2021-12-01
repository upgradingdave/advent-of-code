(ns advent2020.day4
  (:require [clojure.string :as str]))

(def passports
  ["ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\nbyr:1937 iyr:2017 cid:147 hgt:183cm"
   "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\nhcl:#cfa07d byr:1929"
   "hcl:#ae17e1 iyr:2013\neyr:2024\necl:brn pid:760753108 byr:1931\nhgt:179cm"
   "hcl:#cfa07d eyr:2025 pid:166559648\niyr:2011 ecl:brn hgt:59in"]
  )

(def input-file-path "./resources/advent2020/day4.in")

(defn read-passports [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n\n"))

(defn parse-passport [passport-str]
  (let [fields (str/split passport-str #"\s")]
    (into {} (map #(str/split % #":") fields))))

(defn parse-int [s]
  (try (Integer/parseInt s) (catch Exception e nil)))

(defn four-digits? [s]
  (re-matches #"\d{4}" s))

(defn in-range? [s min max]
  (let [v (parse-int s)]
    (when v (and (>= v min) (<= v max)))))

;;If cm, the number must be at least 150 and at most 193.
;;If in, the number must be at least 59 and at most 76.
(defn valid-hgt? [s]
  (cond

    (re-matches #"\d+cm" s)
    (let [[_ v] (re-find #"(\d+)cm" s)]
      (in-range? v 150 193))

    (re-matches #"\d+in" s)
    (let [[_ v] (re-find #"(\d+)in" s)]
      (in-range? v 59 76))

    :else
    false
    
    ))

;;hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
(defn valid-hcl? [s]
  (not (nil? (re-matches #"#[0-9a-f]{6}" s))))

;;ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
(defn valid-ecl? [s]
  (or (= "amb" s)
      (= "blu" s)
      (= "brn" s)
      (= "gry" s)
      (= "grn" s)
      (= "hzl" s)
      (= "oth" s)))

;;pid (Passport ID) - a nine-digit number, including leading zeroes.
(defn valid-pid? [s]
  (not (nil? (re-matches #"\d{9}" s))))

;; byr (Birth Year) - four digits; at least 1920 and at most 2002.
(defn valid-byr? [s]
  (and (four-digits? s)
       (in-range? s 1920 2002)))

;;iyr (Issue Year) - four digits; at least 2010 and at most 2020.
(defn valid-iyr? [s]
  (and (four-digits? s)
       (in-range? s 2010 2020)))

;;eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
(defn valid-eyr? [s]
  (and (four-digits? s)
       (in-range? s 2020 2030)))

(defn field-present? [passport k]
  (not (nil? (get passport k))))

(defn passport-fields-present? [passport]
  (and (field-present? passport "byr")
       (field-present? passport "iyr")
       (field-present? passport "eyr")
       (field-present? passport "hgt")
       (field-present? passport "hcl")
       (field-present? passport "ecl")
       (field-present? passport "pid")
       ;;(field-present? passport "cid")
       ))

(defn passport-valid? [passport]

  (and
   (passport-fields-present? passport)
   (valid-byr? (get passport "byr"))
   (valid-iyr? (get passport "iyr"))
   (valid-eyr? (get passport "eyr"))
   (valid-hgt? (get passport "hgt"))
   (valid-hcl? (get passport "hcl"))
   (valid-ecl? (get passport "ecl"))
   (valid-pid? (get passport "pid")))
  )

(defn day4a [passport-strs]
  (count (filter true?
                 (map passport-fields-present?
                      (map parse-passport passport-strs)))))

(defn day4b [passport-strs]
  (count (filter true?
                 (map passport-valid?
                      (map parse-passport passport-strs)))))

(comment
  (day4a passports)
  (day4a (read-passports input-file-path))

  (day4b passports)
  (day4b (read-passports input-file-path))

  ;; These should all be true if validations are working
  (= true (valid-byr? "2002"))
  (= false (valid-byr? "2003"))

  (= true (valid-hgt? "60in"))
  (= true (valid-hgt? "190cm"))
  (= false (valid-hgt? "190in"))
  (= false (valid-hgt? "190"))

  (= true (valid-hcl? "#123abc"))
  (= false (valid-hcl? "#123abz"))
  (= false (valid-hcl? "123abc"))

  (= true (valid-ecl? "brn"))
  (= false (valid-ecl? "wat"))

  (= true (valid-pid? "000000001"))
  (= false (valid-pid? "0123456789"))


  (= false (passport-valid? (parse-passport "eyr:1972 cid:100\nhcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926")))

  (= false (passport-valid? (parse-passport "iyr:2019\nhcl:#602927 eyr:1967 hgt:170cm\necl:grn pid:012533040 byr:1946")))
  
  )

