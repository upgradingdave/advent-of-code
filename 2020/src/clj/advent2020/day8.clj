(ns advent2020.day8
  (:require [clojure.string :as str]))

(def sample
["nop +0"
 "acc +1"
 "jmp +4"
 "acc +3"
 "jmp -3"
 "acc -99"
 "acc +1"
 "jmp -4"
 "acc +6"])

(def input-file-path "./resources/advent2020/day8.in")

(defn read-program [path-to-input-file]
  (str/split (slurp path-to-input-file) #"\n"))

(defn parse-line [line]
  (let [[cmd n] (str/split line #"\s")]
    [cmd (Integer/parseInt n)]))

(defn parse-program [lines]
  (map-indexed vector (map parse-line lines)))

(defn run-program [instructions last curr hist acc]
  (loop [last last curr curr hist hist acc acc]

    ;; made it to the end of the program!
    (if (>= curr (count instructions))
      [:terminated last curr hist acc]

      (let [instruction (nth instructions curr)
            [idx [cmd n]] instruction]

        ;;(println instruction acc)
        (cond

          ;; has this instruction been run before?
          ;; if so, return accumulator
          (contains? hist curr)
          [:loop-detected last curr hist acc]

          ;; when nop, just go to next instruction
          (= cmd "nop")
          (recur curr (inc curr) (conj hist idx) acc)

          ;; change the accumulator
          (= cmd "acc")
          (recur curr (inc curr) (conj hist idx) (+ acc n))

          ;; jump to instruction
          (= cmd "jmp")
          (recur curr (+ idx n) (conj hist idx) acc)
          
          )))
    )
  )

(defn day8a [instructions]
  (let [[_ _ _ _ acc] (run-program instructions nil 0 #{} 0)]
    acc))

(defn toggle-next-nop-jmp
  "Find the next nop or acc cmd after indx idx and toggle it"
  [instructions idx]
  (loop [idx idx]

    (if (>= idx (count instructions))
      (throw (Exception. "No more instructions?!"))

      (let [[_ [cmd n]] (nth instructions idx)]

        (cond

          (= "nop" cmd)
          [(inc idx)
           (concat (take idx instructions)
                   [[idx ["jmp" n]]]
                   (drop (inc idx) instructions))]

          (= "jmp" cmd)
          [(inc idx)
           (concat (take idx instructions)
                   [[idx ["nop" n]]]
                   (drop (inc idx) instructions))]

          ;; keep looking for nop or jmp
          :else
          (recur (inc idx)))))))

(defn day8b [instructions]
  ;; try running the program
  (let [[r1 l1 c1 h1 acc1] (run-program instructions nil 0 #{} 0)]

    ;; idx is the last index of a nop or jmp command we tried changing
    (loop [idx 0 result r1 acc acc1]

      ;; successully terminated!
      (if (= :terminated result)
        acc

        ;; switch next jmp or nop and try again
        (let [[next-idx new-instructions] (toggle-next-nop-jmp instructions idx)
              [r2 l2 c2 h2 acc2] (run-program new-instructions nil 0 #{} 0)]
          (recur next-idx r2 acc2))))))

(comment
  (day8a (parse-program sample))
  (day8a (parse-program (read-program input-file-path)))

  (day8b (parse-program sample))
  (day8b (parse-program (read-program input-file-path)))
  )
