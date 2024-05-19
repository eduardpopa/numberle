package lu.uni.numberle.ejb;

import jakarta.ejb.Local;

@Local
public interface SolutionGeneratorBeanLocal {
    public Integer[] generate(int size) throws Exception;
}
