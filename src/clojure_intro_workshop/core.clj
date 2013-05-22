(ns clojure-intro-workshop.core)


(defn create-world
  "Return a vector of vectors, which represents a 2D field with the
  given width and height, randomly populated with 0 and 1."
  [width height]
  (for [_ (range height)]
    (for [_ (range width)]
      (rand-int 2))))


(defn world-width [world]
  (count (nth world 0)))


(defn world-height [world]
  (count world))


(defn element-at [world [x y]]
  (if (or (< x 0)
          (< y 0)
          (>= x (world-width world))
          (>= y (world-height world)))
    0 ;; respect array bounds
    (nth (nth world y) x)))


(defn pretty-print-world
  "Handy debugging util."
  [world]
  (doseq [row world]
    (println row)))

(def ppw pretty-print-world) ; alias for easy access in repl


(defn live
  "This function will apply the givne update-fn cycles-times,
  print the steps and return the result"
  ([world update-fn]
    (live world update-fn 2)) ; use a default value of 2

  ([world update-fn cycles]
   (if (zero? cycles)
     ;; true case - just return the world
     world
     ;; false case - update, print and go again
     (let [new-world (update-fn world)] ; build new world
       (println "-------------------------")
       (pretty-print-world new-world) ; print it
       (recur new-world update-fn (dec cycles)))))) ; run again until cycles are zero

;; ------------------
;; Get going there
;; ..................
(defn update-world [world]
  ;; This is your chance to change the world! ;)
  world)  ;; return new world

;; create new world and do 2 cycles
(live (create-world 5 5) update-world 2)
