package bookmarket;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="RegRequest_table")
public class RegRequest {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long bookId;
    private String bookNm;
    private String regYn;
    private Long publId;

    @PostPersist
    public void onPostPersist(){
  /*      
        try {
            Thread.currentThread().sleep((long) (400 + Math.random() * 220));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }        
    */    
        RegRequested regRequested = new RegRequested();
        BeanUtils.copyProperties(this, regRequested);
        regRequested.setRegYn("RegRequested");
        regRequested.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        bookmarket.external.Approve approve = new bookmarket.external.Approve();
        // mappings goes here
        approve.setReqReqId(this.getId());
        approve.setAppYn("RegRequested");
        approve.setPublId(this.publId);
        RegrequestApplication.applicationContext.getBean(bookmarket.external.ApproveService.class)
            .appReq(approve);

    }

    @PreRemove
    public void onPreRemove(){
        ReqCanceled reqCanceled = new ReqCanceled();
        BeanUtils.copyProperties(this, reqCanceled);
        reqCanceled.setRegYn("ReqCanceled");
        reqCanceled.publishAfterCommit();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    public String getBookNm() {
        return bookNm;
    }

    public void setBookNm(String bookNm) {
        this.bookNm = bookNm;
    }
    public String getRegYn() {
        return regYn;
    }

    public void setRegYn(String regYn) {
        this.regYn = regYn;
    }
    public Long getPublId() {
        return publId;
    }

    public void setPublId(Long publId) {
        this.publId = publId;
    }




}
