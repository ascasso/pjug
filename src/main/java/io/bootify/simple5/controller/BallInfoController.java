package io.bootify.simple5.controller;

import io.bootify.simple5.model.BallInfoDTO;
import io.bootify.simple5.service.BallInfoService;
import io.bootify.simple5.util.ReferencedWarning;
import io.bootify.simple5.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/ballInfos")
public class BallInfoController {

    private final BallInfoService ballInfoService;

    public BallInfoController(final BallInfoService ballInfoService) {
        this.ballInfoService = ballInfoService;
    }

    @GetMapping
    public String list(@RequestParam(name = "filter", required = false) final String filter,
            @SortDefault(sort = "ballId") @PageableDefault(size = 20) final Pageable pageable,
            final Model model) {
        final Page<BallInfoDTO> ballInfoes = ballInfoService.findAll(filter, pageable);
        model.addAttribute("ballInfoes", ballInfoes);
        model.addAttribute("filter", filter);
        model.addAttribute("paginationModel", WebUtils.getPaginationModel(ballInfoes));
        return "ballInfo/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("ballInfo") final BallInfoDTO ballInfoDTO) {
        return "ballInfo/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("ballInfo") @Valid final BallInfoDTO ballInfoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ballInfo/add";
        }
        ballInfoService.create(ballInfoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ballInfo.create.success"));
        return "redirect:/ballInfos";
    }

    @GetMapping("/edit/{ballId}")
    public String edit(@PathVariable(name = "ballId") final Long ballId, final Model model) {
        model.addAttribute("ballInfo", ballInfoService.get(ballId));
        return "ballInfo/edit";
    }

    @PostMapping("/edit/{ballId}")
    public String edit(@PathVariable(name = "ballId") final Long ballId,
            @ModelAttribute("ballInfo") @Valid final BallInfoDTO ballInfoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ballInfo/edit";
        }
        ballInfoService.update(ballId, ballInfoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("ballInfo.update.success"));
        return "redirect:/ballInfos";
    }

    @PostMapping("/delete/{ballId}")
    public String delete(@PathVariable(name = "ballId") final Long ballId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = ballInfoService.getReferencedWarning(ballId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            ballInfoService.delete(ballId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("ballInfo.delete.success"));
        }
        return "redirect:/ballInfos";
    }

}
