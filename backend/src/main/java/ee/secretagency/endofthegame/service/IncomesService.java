package ee.secretagency.endofthegame.service;

import ee.secretagency.endofthegame.entity.Income;
import ee.secretagency.endofthegame.exception.IncomeNotFoundException;
import ee.secretagency.endofthegame.repository.IncomesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class IncomesService {

    private final IncomesRepository repository;

    public IncomesService(IncomesRepository repository) {
        this.repository = repository;
    }

    public List<Income> readAllIncomes() {
//        List<Income> result = repository.findAll();
        var incomesFromDb = repository.findAll();

        log.info("incomes from datasource: {}", incomesFromDb);
        return incomesFromDb;
    }

    public Income readIncomeById(Long id) {
        log.info("reading income with id: [{}]", id);
        Income incomeFromRepository = null;
        try {
            incomeFromRepository = repository.getOne(id);
            if (incomeFromRepository == null) {
                log.info("It's null");
            } else {
                log.info("It's not null");
            }
            log.info("" + incomeFromRepository);
            log.info("read income: [{}]", incomeFromRepository);

        } catch (EntityNotFoundException e) {
            log.warn("some unexpected exception has occurred", e);
            return null;
        }
        return incomeFromRepository;
    }

    public Income readIncomeByIdBetterWay(Long id) {
        log.info("reading income with id: [{}]- better way", id);
        var maybeIncome = repository.findById(id);
//        return maybeIncome.orElseThrow(new Supplier<Throwable>() {
//            @Override
//            public Throwable get() {
//                return new EntityNotFoundException("No entity with id: [{%d}]".formatted(id));
//            }
//        });

        return maybeIncome.orElseThrow(() -> new IncomeNotFoundException("No entity with id: [{%d}]".formatted(id)));
    }

    public void deleteIncomeWithId(Long id) {
        log.info("deleting income with id: [{}]", id);
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException exc) {
            log.warn("Trying to delete non existent income", exc);
            throw new IncomeNotFoundException("No existing income", exc);
        }
    }

    @Transactional
    public void deleteIncomeWithIdBetter(Long id) {
        log.info("deleting income with id: [{}]", id);

        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new IncomeNotFoundException("No entity with id: [{%d}]".formatted(id));
        }
    }

    public Income createNewIncome(Income income) {
        log.info("object before saving: [{}]", income);
        if (income.getTimestamp() == null) {
            income.setTimestamp(ZonedDateTime.now());
        }
        Income saved = repository.save(income);
        log.info("object after saving: [{}]", saved);
        log.info("input income after saving: [{}]", income);

        return saved;
    }
}
