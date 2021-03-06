package com.ladders.oc.applications;

import com.ladders.oc.jobseekers.Jobseeker;
import com.ladders.oc.recruiters.JobPosting;
import com.ladders.oc.resumes.Resume;
import com.theladders.confident.Maybe;

public class ApplicationProcessor
{
  private final ApplicationRepository applicationRepository;
  private final TimeServer timeServer;

  public ApplicationProcessor(ApplicationRepository appRepo, TimeServer timeServer)
  {
    applicationRepository = appRepo;
    this.timeServer = timeServer;
  }

  public boolean apply(Jobseeker  jobseeker,
                       JobPosting jobPosting,
                       Maybe<Resume> resume)
  {
    if (!isValidApplication(jobseeker, jobPosting, resume))
      return false;
    
    Application app = new Application(jobPosting, jobseeker, timeServer.getCurrentTime());
    applicationRepository.addApplication(app);

    return true;
  }
  
  private boolean isValidApplication(Jobseeker  jobseeker,
                                     JobPosting jobPosting,
                                     Maybe<Resume> maybeResume)
  {
    if (jobPosting.RequiresResume())
    {
      if (maybeResume.isNothing())
        return false;
  
      Resume resume = maybeResume.get();
      if (!resume.ownedBy(jobseeker))
          return false;
    }

    return true;
  }

}
