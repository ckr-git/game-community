package com.game.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.game.community.common.Result;
import com.game.community.entity.Announcement;
import com.game.community.entity.Banner;
import com.game.community.entity.GameCategory;
import com.game.community.mapper.AnnouncementMapper;
import com.game.community.mapper.BannerMapper;
import com.game.community.mapper.GameCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/common")
public class CommonController {
    
    @Autowired
    private AnnouncementMapper announcementMapper;
    
    @Autowired
    private BannerMapper bannerMapper;
    
    @Autowired
    private GameCategoryMapper gameCategoryMapper;
    
    // ========== 公告 ==========
    
    /**
     * 获取公告列表
     */
    @GetMapping("/announcements")
    public Result<List<Announcement>> getAnnouncements(
            @RequestParam(required = false) Integer type) {
        
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        
        queryWrapper.orderByDesc("create_time");
        List<Announcement> announcements = announcementMapper.selectList(queryWrapper);
        
        return Result.success(announcements);
    }
    
    /**
     * 获取公告详情
     */
    @GetMapping("/announcements/{id}")
    public Result<Announcement> getAnnouncementDetail(@PathVariable Long id) {
        return Result.success(announcementMapper.selectById(id));
    }
    
    /**
     * 新增公告（管理员）
     */
    @PostMapping("/announcements")
    public Result<String> addAnnouncement(@RequestBody Announcement announcement) {
        announcement.setStatus(1);
        announcementMapper.insert(announcement);
        return Result.success("添加成功");
    }
    
    /**
     * 更新公告（管理员）
     */
    @PutMapping("/announcements/{id}")
    public Result<String> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        announcement.setId(id);
        announcementMapper.updateById(announcement);
        return Result.success("更新成功");
    }
    
    /**
     * 删除公告（管理员）
     */
    @DeleteMapping("/announcements/{id}")
    public Result<String> deleteAnnouncement(@PathVariable Long id) {
        announcementMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // ========== 轮播图 ==========
    
    /**
     * 获取轮播图列表
     */
    @GetMapping("/banners")
    public Result<List<Banner>> getBanners() {
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort_order");
        
        List<Banner> banners = bannerMapper.selectList(queryWrapper);
        return Result.success(banners);
    }
    
    /**
     * 新增轮播图（管理员）
     */
    @PostMapping("/banners")
    public Result<String> addBanner(@RequestBody Banner banner) {
        banner.setStatus(1);
        bannerMapper.insert(banner);
        return Result.success("添加成功");
    }
    
    /**
     * 更新轮播图（管理员）
     */
    @PutMapping("/banners/{id}")
    public Result<String> updateBanner(@PathVariable Long id, @RequestBody Banner banner) {
        banner.setId(id);
        bannerMapper.updateById(banner);
        return Result.success("更新成功");
    }
    
    /**
     * 删除轮播图（管理员）
     */
    @DeleteMapping("/banners/{id}")
    public Result<String> deleteBanner(@PathVariable Long id) {
        bannerMapper.deleteById(id);
        return Result.success("删除成功");
    }
    
    // ========== 游戏分类 ==========
    
    /**
     * 获取游戏分类列表
     */
    @GetMapping("/categories")
    public Result<List<GameCategory>> getCategories() {
        QueryWrapper<GameCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort_order");
        
        List<GameCategory> categories = gameCategoryMapper.selectList(queryWrapper);
        return Result.success(categories);
    }
    
    /**
     * 新增分类（管理员）
     */
    @PostMapping("/categories")
    public Result<String> addCategory(@RequestBody GameCategory category) {
        gameCategoryMapper.insert(category);
        return Result.success("添加成功");
    }
    
    /**
     * 更新分类（管理员）
     */
    @PutMapping("/categories/{id}")
    public Result<String> updateCategory(@PathVariable Long id, @RequestBody GameCategory category) {
        category.setId(id);
        gameCategoryMapper.updateById(category);
        return Result.success("更新成功");
    }
    
    /**
     * 删除分类（管理员）
     */
    @DeleteMapping("/categories/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        gameCategoryMapper.deleteById(id);
        return Result.success("删除成功");
    }
}
