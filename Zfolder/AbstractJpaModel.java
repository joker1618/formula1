public abstract class AbstractJpaModel {

@Autowired
protected F1QualifyDao f1QualifyDao

@Autowired
protected F1EntrantDao f1EntrantDao

@Autowired
protected RepoPropertyDao repoPropertyDao

@Autowired
protected RepoUriDao repoUriDao

@Autowired
protected F1CircuitDao f1CircuitDao

@Autowired
protected F1CountryDao f1CountryDao

@Autowired
protected F1DriverDao f1DriverDao

@Autowired
protected F1SeasonPointsDao f1SeasonPointsDao

@Autowired
protected F1GranPrixDao f1GranPrixDao

@Autowired
protected F1TeamDao f1TeamDao

@Autowired
protected F1RaceDao f1RaceDao

@Autowired
protected RepoResourceDao repoResourceDao

}