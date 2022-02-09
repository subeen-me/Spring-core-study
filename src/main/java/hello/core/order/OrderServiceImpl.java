package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    //final이 되면 무조건 생성자로 할당이 되어야한다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy; // 인터페이스에만 의존하도록 변경. DIP를 지키고 있다. 구체적 구현체는 모름

 //   private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
 //   private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    // 롬복 사용하면 롬복이 알아서 만들어주는 코드 @RequiredArgsConstructor
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        // 회원정보 조회
        Member member = memberRepository.findById(memberId);
        // 정책에 넘긴다. 여기서 직접 회원등급 확인하지 않음! 역할 분리.
        int discountPrice = discountPolicy.discount(member, itemPrice);

        // 주문 만들어서 반환
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //test
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
