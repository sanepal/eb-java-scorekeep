package scorekeep;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import java.util.List;

public class SessionModel {
  /** AWS SDK credentials. */
  private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
        .withRegion(Regions.fromName(System.getenv("AWS_REGION")))
        .build();
  private DynamoDBMapper mapper = new DynamoDBMapper(client);

  public void saveSession(Session session) {    // wrap in subsegment
    mapper.save(session);
  }

  public Session loadSession(String sessionId) throws SessionNotFoundException {
    Session session = mapper.load(Session.class, sessionId);
    if ( session == null ) {
      throw new SessionNotFoundException(sessionId);
    }
    return session;
  }

  public List<Session> loadSessions(){
    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
    List<Session> scanResult = mapper.scan(Session.class, scanExpression);
    return scanResult;
  }

  public void deleteSession(String sessionId) throws SessionNotFoundException {
    Session session = mapper.load(Session.class, sessionId);
    if ( session == null ) {
      throw new SessionNotFoundException(sessionId);
    }
    mapper.delete(session);
  }
}