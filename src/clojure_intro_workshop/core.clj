(ns clojure-intro-workshop.core
  (:use quil.core)) ;; quil is our drawing library

(defn create-world
  "Return a vector of vectors that builds a 2D field
  with the given width and height, randomly populated with 0 and 1."
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

;; setting up state
;; --------------------------
(def world (agent (create-world 50 40)))

(defn update-world [world]
  ; This is your chance to change the world! ;)
  world) ;; return new state

(def tick-delay 2000)

(defn tick [world]
    ;; *agent* is dynamically bound to the agent this function was sent to
    (send *agent* update-world)
    (Thread/sleep tick-delay) ;; delay the update
    (send *agent* tick) ;; redo this
    world) ;; always return a state that should be bound

(defn start-main-loop []
  (send world tick))

;; rendering functions
;; --------------------------

;; called only once
(defn setup []
  (smooth)
  (frame-rate 20)
  (background 0)) ;; black background

;; called for each frame
(defn draw []
  (stroke 0)
  (stroke-weight 0)
  (let [tile-width (/ (width) (world-width @world))
        tile-height (/ (height) (world-height @world))]
    (dorun ;; force evaluation of lazy sequenz
      (for [x (range (world-width @world))
            y (range (world-height @world))]
        (do ;; we do our drawing side effect here
          ;; set fill based on cell alive state
          (fill (* 255 (element-at @world [x y])))
          ;; draw a rect for each cell
          (rect (* x tile-width) (* y tile-height)
                tile-width tile-height))))))


;; main function
;; --------------------------
(defn -main []
  ;; this is quil starting a drawing cycle
  (defsketch example
    :title "Sketch"
    :setup setup
    :draw draw
    :size [(* (world-width @world) 15) (* (world-height @world) 15)])
  ;; start the never-ending main loop
  (start-main-loop))
