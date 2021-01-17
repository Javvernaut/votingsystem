####**Вопросы:**
Как правильнее создавать DTO?

Так?

    @Query("SELECT new javvernaut.votingsystem.to.RestaurantTo(r.id, r.name, COUNT (v)) " +
            "FROM Restaurant r " +
            "LEFT JOIN r.votes v " +
            "INNER JOIN r.menus m " +
            "WHERE m.date=:date " +
            "GROUP BY r ORDER BY COUNT(v) DESC ")
    List<RestaurantTo> findAllTosWithVotesByMenuDate(LocalDate date);
    
    ...

    @GetMapping
    public List<RestaurantTo> getAllWithVotesCountForCurrentDay() {
        log.info("get all restaurants that presented menu for current date");
        return restaurantRepository.findAllTosWithVotesByMenuDate(CURRENT_DATE);

Или так?

    @EntityGraph(attributePaths = {"votes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r INNER JOIN r.menus m WHERE m.date=:date")
    List<Restaurant> findAllTosWithVotesByMenuDate(LocalDate date);

    ...

    @GetMapping
    public List<RestaurantTo> getAll() {
        List<Restaurant> restaurants = restaurantRepository.findAllTosWithVotesByMenuDate(CURRENT_DATE);
        return restaurants.stream()
                .map(RestaurantUtil::asTo)
                .sorted(Comparator.comparing(RestaurantTo::getVotes).reversed())
                .collect(Collectors.toList());
    }