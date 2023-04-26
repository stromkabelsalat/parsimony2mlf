package name.stromkabelsalat.parsimony2mlf.mlf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import name.stromkabelsalat.parsimony2mlf.mlf.entities.Entry;

public interface EntryRepository extends JpaRepository<Entry, Integer> {
}
