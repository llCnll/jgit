package cn.chennan.jgit.helper;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author ChenNan
 * @date 2019-12-13 下午12:24
 **/
@Slf4j
@Component
public class GitUtil implements InitializingBean, DisposableBean {
    private static Repository repo;
    private static Git git;

    @Value("${git.repo-dir}")
    private String repoUrl;
    @Value("${git.remote-url}")
    private String remoteUrl;

    public static void pull() throws Exception{
        PullResult call = git.pull().setRemoteBranchName("master").setRemote("origin").call();
        if(call.isSuccessful()){
            log.info("pull successfully");
        }
    }

    public static void commit(String prefixUrl, String name, String comment) throws Exception {
        File file = new File(repo.getDirectory().getParentFile(), name);
        if(!file.exists()){
            throw new Exception("file not exist, add fault, "+ file.getPath());
        }

        git.add().addFilepattern(prefixUrl + "/" + name).call();
        RevCommit revCommit = git.commit().setMessage(comment).call();
        ObjectId id = revCommit.getId();
        log.info("comment successfully, comment id : {}", id);
    }

    public static void push() throws Exception{
        git.push().call();
        log.info("push successfully");
    }
    public static void commitAndPush(String prefixUrl, String name, String comment) throws Exception{
        commit(prefixUrl, name, comment);
        push();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 先判断.git是否存在, 不存在则进行克隆
        log.info("Git init");
        try {
            File repoDir = new File(repoUrl);
            if(!repoDir.exists()){
                git = Git.cloneRepository().setURI(remoteUrl)
                        .setDirectory(repoDir.getParentFile()).call();
                log.info("Cloning from " + remoteUrl + " to " + git.getRepository());
            }
            repo = new FileRepositoryBuilder()
                    .setGitDir(repoDir)
                    .build();
            log.info("repo build.");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        git = new Git(repo);
    }

    @Override
    public void destroy() throws Exception {
        git.close();
        repo.close();
    }
}
