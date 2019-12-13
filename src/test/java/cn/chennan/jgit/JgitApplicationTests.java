package cn.chennan.jgit;

import cn.chennan.jgit.helper.GitUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class JgitApplicationTests {

    //--------api-------------------------
    @Test
    public void create() throws Exception{
        // 创建一个新仓库
        Repository newlyCreatedRepo = FileRepositoryBuilder.create(
                new File("/home/chennan02/practiceGIT/.git"));
        newlyCreatedRepo.create();
    }

    @Test
    public void open() throws Exception {
        // 打开一个存在的仓库
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(new File("/home/chennan02/practiceGIT/.git"))
                .build();
        Git git = new Git(repo);
    }
    @Test
    public void gitClone(){
        String remoteUrl = "git@github.com:llCnll/practiceJGit.git";
        File repoDir = new File("/home/chennan02/practiceGIT");
        try {
            Git git = Git.cloneRepository()
                    .setURI(remoteUrl)
                    .setDirectory(repoDir)
                    .call();

            log.info("Cloning from " + remoteUrl + " to " + git.getRepository());
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void gitAdd() throws Exception{
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(new File("/home/chennan02/practiceGIT/.git"))
                .build();
        Git git = new Git(repo);
        git.add().addFilepattern("README.md").call();
    }

    @Test
    public void gitCommit() throws Exception{
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(new File("/home/chennan02/practiceGIT/.git"))
                .build();

        Git git = new Git(repo);
        git.commit().setMessage("add readme").call();
    }

    @Test
    public void gitPush() throws Exception{
        Repository repo = new FileRepositoryBuilder()
                .setGitDir(new File("/home/chennan02/practiceGIT/.git"))
                .build();
        Git git = new Git(repo);
        git.push().call();
    }
    //------------myutil------------------
    @Test
    public void t() throws Exception{
        GitUtil.commit("", "addfile.txt", "addfile");
    }
    @Test
    public void push2() throws Exception{
        GitUtil.commitAndPush("", "addfile.txt", "update file");
    }

    @Test
    public void pull() throws Exception{
        GitUtil.pull();
    }
}
