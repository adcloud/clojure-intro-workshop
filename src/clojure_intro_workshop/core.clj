(ns clojure-intro-workshop.core
  (:use quil.core))


(defn create-world
  "Return a vector of vectors, which represents a 2D field with the
  given width and height, randomly populated with 0 and 1."
  [width height]
  (for [_ (range height)]
    (for [_ (range width)]
      (rand-int 2))))


(defn create-blinker[]
  [[0 1 0]
   [0 1 0]
   [0 1 0]])


(defn create-glider []
  [[0 0 0 1 0 0 0 0 0 0 0]
   [0 1 0 1 0 0 0 0 0 0 0]
   [0 0 1 1 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]
   [0 0 0 0 0 0 0 0 0 0 0]])


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
    (println (map (fn [cell] (if (zero? cell) " " "+"))
                  row))))

(def ppw pretty-print-world) ; alias for easy access in repl


(defn live
  "This function will apply the givne update-fn cycles-times,
  print the steps and return the result"
  [world update-fn cycles]
   (if (zero? cycles)
     ;; true case - just return the world
     world
     ;; false case - update, print and go again
     (let [new-world (update-fn world)] ; build new world
       (println)
       (pretty-print-world new-world) ; print it
       (update-world new-world) ;; display each step of the world
       (recur new-world update-fn (dec cycles))))) ; run again until cycles are zero

;; ------------------
;; Get going there
;; ..................
(defn update-world [world]
  ;; This is your chance to change the world! ;)
  world)  ;; return new world

;; create new world and do 2 cycles
; (live (create-blinker) update-world 2)

(def w (create-blinker))

;; this is a no go in normal development but for a repl it will do
;; by running this again and again you can see the change over time
(def w (live w update-world 1))



;; #####################################################
;; here be dragons

(def rendered-w (atom [[1 0]
                       [0 1]]))

(def update-world [w]
  (reset! rendered-w))

;; called only once
(defn setup []
  (smooth)
  (frame-rate 20)
  (background 0)) ;; black background

;; called for each frame
(defn draw []
  (stroke 0)
  (stroke-weight 0)
  (let [tile-width (/ (width) (world-width rendered-w))
        tile-height (/ (height) (world-height rendered-w))]
    (dorun  ;; force immediate evaluation of lazy sequence
      (for [x (range (world-width rendered-w))
            y (range (world-height rendered-w))]
        (do  ;; we do our drawing side-effects here
          ;; set fill based on cell alive state
          (fill (* 255 (element-at rendered-w [x y])))
          ;; draw a rect for each cell
          (rect (* x tile-width) (* y tile-height)
                tile-width tile-height))))))


;; main function
;; --------------------------
(defn start-rendering []
  ;; this is quil starting a drawing cycle
  (defsketch example
    :title "Game of Life"
    :setup setup
    :draw draw
    :size [(* (world-width rendered-w) 15),
           (* (world-height rendered-w) 15)]))

(def w (create-world 3 7))
(start-rendering)
