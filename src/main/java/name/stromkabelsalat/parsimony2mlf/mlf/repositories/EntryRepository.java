package name.stromkabelsalat.parsimony2mlf.mlf.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import name.stromkabelsalat.parsimony2mlf.mlf.entities.Entry;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
	List<Entry> findByTid(Integer tid);
}
