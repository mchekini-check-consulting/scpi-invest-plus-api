
import java.util.List;
import java.util.Optional;

public interface InvestorService {
    Investor createInvestor(InvestorDTO investorDTO);

    List<InvestorDTO> getAllInvestors();
    Investor updateInvestor(String email, InvestorDTO investorDTO);
    Optional<Investor> getInvestorByEmail(String email);
}